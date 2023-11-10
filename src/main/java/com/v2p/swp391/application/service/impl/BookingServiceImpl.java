package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.BookingHttpMapper;
import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.request.PaymentRequest;
import com.v2p.swp391.application.response.BookingResponse;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.application.service.BookingDetailService;
import com.v2p.swp391.application.service.BookingService;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.payment.PaymentService;
import com.v2p.swp391.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class    BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingDetailService bookingDetailService;
    private final BookingDetailRepository bookingDetailRepository;
    private final PaymentService paymentService;
    private final BirdRepository birdRepository;
    private final PaymentRepositorty paymentRepository;
    private final BookingHttpMapper bookingMapper;

    @Override
    public Booking createBooking(Booking booking, BookingDetail bookingDetail) {
        User user = userRepository
                        .findById(booking.getUser().getId())
                        .orElseThrow(()-> new ResourceNotFoundException("User", "id", booking.getUser().getId()));

        Long id = bookingDetail.getFatherBird().getId();
        Bird fatherBird = birdRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Bird", "id", bookingDetail.getFatherBird().getId()));
        id = bookingDetail.getMotherBird().getId();
        Bird motherBird = birdRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Bird", "id", bookingDetail.getMotherBird().getId()));
        Booking createdBooking = bookingRepository.save(booking);
        bookingDetailService.createBookingDetail(createdBooking, bookingDetail);
        this.updateTotalPaymentBooking(createdBooking.getId(),fatherBird.getPrice() + motherBird.getPrice());

        booking.setStatus(BookingStatus.Pending);
        return bookingRepository.save(booking);
    }

    @Override
    public BookingResponse createBookingHavePayment(Booking booking, BookingDetail bookingDetail) throws UnsupportedEncodingException {
        //Create Booking
        Booking createdBooking = createBooking(booking, bookingDetail);
        BookingResponse bookingResponse =  new BookingResponse();
        bookingResponse.setBookingId(createdBooking.getId());
        bookingResponse.setBooking(createdBooking);

        //Create payment for Booking
        PaymentRequest paymentRequest = new PaymentRequest(booking.getPaymentDeposit(), PaymentForType.DEPOSIT_BOOKING, bookingResponse.getBookingId());
        PaymentRespone paymentRespone = paymentService
                .createPayment(paymentRequest.getAmount(), paymentRequest.getPaymentForType(), paymentRequest.getId());
        bookingResponse.setPaymentRespone(paymentRespone);
        return bookingResponse;
    }

    @Override
    public PaymentRespone payUnpaidDepositMoney(Long id) throws UnsupportedEncodingException {
        PaymentRespone paymentRespone = this.payMoney(id, PaymentForType.DEPOSIT_BOOKING);
        return paymentRespone;
    }

    @Override
    public PaymentRespone payTotalMoney(Long id) throws UnsupportedEncodingException {
        PaymentRespone paymentRespone = this.payMoney(id, PaymentForType.TOTAL_BOOKING);
        return paymentRespone;
    }

    private PaymentRespone payMoney(Long id, PaymentForType paymentForType) throws UnsupportedEncodingException {
        Booking booking = bookingRepository.findBookingById(id);
        PaymentRespone paymentRespone = new PaymentRespone();
        if(paymentForType.equals(PaymentForType.DEPOSIT_BOOKING)){
            paymentRespone = paymentService.createPayment(booking.getPaymentDeposit(), paymentForType, id);
        }
        else if (paymentForType.equals(PaymentForType.TOTAL_BOOKING)){
            paymentRespone = paymentService.createPayment(booking.getTotalPayment() - booking.getPaymentDeposit(), paymentForType, id);
        }
        return paymentRespone;
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find booking with id: " + id));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByUserId(Long id) {
        return bookingRepository.findByUserId(id);
    }

    private boolean checkFormatStatus(Booking booking, BookingStatus status){
        List<Object> statuses = new ArrayList<Object>();
        statuses.add(BookingStatus.Pending);
        statuses.add(BookingStatus.Confirmed);
        statuses.add(BookingStatus.Preparing);
        statuses.add(BookingStatus.Shipping);
        statuses.add(BookingStatus.Delivered);
        statuses.add(BookingStatus.Cancelled);

        int bookingStatusIndex = -1;
        int statusIndex = 1;
        for (int i = 0; i < statuses.size(); i++){
            if(statuses.get(i).equals(status)) statusIndex = i;
            if(statuses.get(i).equals(booking.getStatus())) bookingStatusIndex = i;
        }

        //Status should follow flow: Pending -> Confirmed -> Completed -> Cancelled
        //Correct status format
        if(statusIndex == -1 || statusIndex < bookingStatusIndex)
            return false;
        return true;
    }
    @Override
    public Booking updateStatusBooking(Long bookingId, BookingStatus status) {
        Booking existingBooking = getBookingById(bookingId);
        if(!checkFormatStatus(existingBooking, status))
            throw new AppException(HttpStatus.BAD_REQUEST, "Status is wrong format");

        if(status.equals(BookingStatus.Confirmed)){
            existingBooking.getBookingDetail().setStatus(BookingDetailStatus.In_Breeding_Progress);
            bookingDetailRepository.save(existingBooking.getBookingDetail());
        }

        existingBooking.setStatus(status);
        return bookingRepository.save(existingBooking);
    }

    @Override
    public Booking updateTotalPaymentBooking(Long bookingId, float total) {
        Booking existingBooking = getBookingById(bookingId);
        existingBooking.setTotalPayment(total);
        existingBooking.setPaymentDeposit((float) (total * 0.3));
        return bookingRepository.save(existingBooking);
    }

    @Override
    public Booking updateTimeBooking(Long bookingId, String dateString) {
        Booking existingBooking = getBookingById(bookingId);
        LocalDateTime parsedTimestamp = DateUtils.parseTimestamp(dateString);
        existingBooking.setBookingTime(parsedTimestamp);
        return bookingRepository.save(existingBooking);
    }


    @Override
    public Booking deleteBooking(Long id) {
        Booking existingBooking = getBookingById(id);
        if(existingBooking.getBookingDetail() != null)
            bookingDetailService.deleteBookingDetail(existingBooking.getBookingDetail().getId());
        bookingRepository.deleteById(id);
        return existingBooking;
    }
}
