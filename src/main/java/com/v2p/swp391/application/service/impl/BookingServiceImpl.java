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
import com.v2p.swp391.security.UserPrincipal;
import com.v2p.swp391.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final SendEmailServiceImpl sendEmailService;
    private final CategoryRepository categoryRepository;
    private final BirdPairingRepository birdPairingRepository;

    @Override
    public Booking createBooking(Booking booking, BookingDetail bookingDetail) {
        User user = userRepository
                        .findById(booking.getUser().getId())
                        .orElseThrow(()-> new ResourceNotFoundException("User", "id", booking.getUser().getId()));

        Bird fatherBird = birdRepository
                .findById(bookingDetail.getFatherBird().getId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("Bird", "id", bookingDetail.getFatherBird().getId()));
        Bird motherBird = birdRepository
                .findById(bookingDetail.getMotherBird().getId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("Bird", "id", bookingDetail.getMotherBird().getId()));
        Booking createdBooking = bookingRepository.save(booking);
        bookingDetailService.createBookingDetail(createdBooking, bookingDetail);

        float totalPayment = fatherBird.getPrice() + motherBird.getPrice() + booking.getShippingMoney();
        float totalMoney = fatherBird.getPrice() + motherBird.getPrice();
        this.updateTotalPaymentBooking(createdBooking.getId(), totalMoney);
        createdBooking.setTotalPayment(totalPayment);

        fatherBird.setQuantity(fatherBird.getQuantity()-1);
        motherBird.setQuantity(motherBird.getQuantity()-1);
        birdRepository.save(fatherBird);
        birdRepository.save(motherBird);

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

        //Create payment for Deposit Booking
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
        Booking existingBooking = bookingRepository.findBookingById(id);
        PaymentRespone paymentRespone = this.payMoney(id, PaymentForType.TOTAL_BOOKING);
        sendEmailService.sendMailPayment(existingBooking.getUser(), paymentRespone.getURL());
        return paymentRespone;
    }

    private void sendEmailShippingNotification(Long id){
        Booking existingBooking = bookingRepository.findBookingById(id);
        sendEmailService.sendShippingNotificationBooking(existingBooking.getUser());
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
    @Scheduled(fixedRate = 86400000)
    public void automaticallySetCancelledBooking() {
        List<Booking> shippingBookingList = getShippingBooking();
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime cprDate = currentDate.minusDays(15);

        for(Booking booking : shippingBookingList){
            if(booking.getUpdatedAt().isBefore(cprDate)){
                booking.setStatus(BookingStatus.Cancelled);
                bookingRepository.save(booking);
            }
        }
    }

//    @Override
//    @Scheduled(fixedRate = 86400000)
//    public void automatically

    @Override
    @Scheduled(fixedRate = 86400000)
    public void automaticallySetBirdCategoryFromCancelledBooking() {
        List<Bird> fledglingBirds = this.getBirdFromCanceledBooking();
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime cprDate = currentDate.minusDays(60);
        for(Bird bird : fledglingBirds){
            if (bird.getCreatedAt().isBefore(cprDate)){
                Category category = categoryRepository.findByName("Chim F1");
                if(category == null){
                    category = new Category();
                    category.setName("Chim F1");
                    categoryRepository.save(category);
                }
                bird.setCategory(category);
                birdRepository.save(bird);
            }
        }

    }

    private List<Booking> getShippingBooking(){
        List<Booking> shippingBookingList = new ArrayList<>();
        for(Booking booking : getAllBookings()){
            if(booking.getStatus().equals(BookingStatus.Shipping)){
                shippingBookingList.add(booking);
            }
        }
        return shippingBookingList;
    }

    private List<Bird> getBirdFromCanceledBooking(){
        List<Bird> fledglingBird = new ArrayList<>();
        List<Bird> allBird = birdRepository.findAll();
        for(Bird bird : allBird){
            if(bird.getCategory() == null){
                BirdPairing birdPairing = birdPairingRepository.findByNewBird_Id(bird.getId());
                Booking booking = birdPairing.getBookingDetail().getBooking();
                if(booking.getStatus().equals(BookingStatus.Cancelled)){
                    fledglingBird.add(bird);
                }
            }

        }
        return fledglingBird;
    }


    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByUserId(Long id) {
        return bookingRepository.findByUserId(id);
    }

    @Override
    public List<Booking> getBookingByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        Sort sortByCreatedAtDesc = Sort.by(Sort.Order.desc("createdAt"));

        List<Booking> bookings = bookingRepository.findByUserId(user.getId(), sortByCreatedAtDesc);
        return bookings;
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
    public Booking updateStatusBooking(Long bookingId, BookingStatus status) throws UnsupportedEncodingException {
        Booking existingBooking = getBookingById(bookingId);
        if(!checkFormatStatus(existingBooking, status))
            throw new AppException(HttpStatus.BAD_REQUEST, "Status is wrong format");

        if(status.equals(BookingStatus.Confirmed)){
            existingBooking.getBookingDetail().setStatus(BookingDetailStatus.In_Breeding_Progress);
            bookingDetailRepository.save(existingBooking.getBookingDetail());
        }
        else if (status.equals(BookingStatus.Preparing)){
            if(existingBooking.getPaymentMethod().equals(PaymentMethod.Debit_Or_Credit_Card)){
                this.payTotalMoney(existingBooking.getId());
            }
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
        if(existingBooking.getBookingDetail() != null) {
            bookingDetailService.deleteBookingDetail(existingBooking.getBookingDetail().getId());
            existingBooking.getBookingDetail().getFatherBird().setQuantity((existingBooking.getBookingDetail().getFatherBird().getQuantity()) + 1);
            existingBooking.getBookingDetail().getMotherBird().setQuantity((existingBooking.getBookingDetail().getFatherBird().getQuantity()) + 1);
        }
        bookingRepository.deleteById(id);
        return existingBooking;
    }

    @Override
    public Booking updateTrackingNumber(Long id, String trackingNumber) {
        Booking existingBooking = getBookingById(id);
        existingBooking.setTrackingNumber(trackingNumber);
        existingBooking.setShippingDate(java.time.LocalDate.now());
        existingBooking.setStatus(BookingStatus.Shipping);
        this.sendEmailShippingNotification(existingBooking.getId());
        bookingRepository.save(existingBooking);
        return existingBooking;
    }
}
