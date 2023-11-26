package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.mapper.BookingDetailHttpMapper;
import com.v2p.swp391.application.mapper.BookingHttpMapper;
import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.BookingStatus;
import com.v2p.swp391.application.request.BookingDetailRequest;
import com.v2p.swp391.application.request.BookingRequest;
import com.v2p.swp391.application.response.BookingResponse;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.application.service.impl.BookingServiceImpl;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.v2p.swp391.application.mapper.BookingHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;

    @PostMapping("")
    public CoreApiResponse<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest bookingRequest
    ) throws IOException {
        BookingResponse bookingRespone = bookingService
                .createBookingHavePayment(
                        BookingHttpMapper.INSTANCE.toModel(bookingRequest),
                        BookingDetailHttpMapper.INSTANCE.toModel(bookingRequest.getBookingDetailRequest()));
        return CoreApiResponse.success(bookingRespone, "Insert booking sucessfully!");
    }

    @GetMapping("")
    public CoreApiResponse<List<Booking>> getAllBookings (){
        List<Booking> bookings = bookingService.getAllBookings();
        return CoreApiResponse.success(bookings);
    }

    @GetMapping("/users/{id}")
    public CoreApiResponse<List<Booking>> getBookingByUserId (
            @Valid @PathVariable Long id
    ){
        List<Booking> bookings = bookingService.getBookingsByUserId(id);
        return CoreApiResponse.success(bookings);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER')")
    public CoreApiResponse<List<Booking>> getBookingByUser(){
        List<Booking> bookings = bookingService.getBookingByUser();
        return CoreApiResponse.success(bookings, "Successfully");
    }

    @GetMapping("/{id}")
    public CoreApiResponse<Booking> getBookingById (
            @Valid @PathVariable Long id
    ){
        Booking booking = bookingService.getBookingById(id);
        return CoreApiResponse.success(booking);
    }

    @PutMapping("/{id}/status")
    public CoreApiResponse<Booking> updateStatusBooking(
            @Valid @PathVariable Long id,
            @Valid @RequestParam("status") BookingStatus status
    ) throws UnsupportedEncodingException {
        Booking updatedBooking = bookingService.updateStatusBooking(id, status);
        return CoreApiResponse.success(updatedBooking, "Update booking id: " + id + " successfully!");
    }

    @PutMapping("{id}/bookingtime")
    public CoreApiResponse<Booking> updateBookingTime(
        @Valid @PathVariable Long id,
        @Valid @RequestParam("newTimescamp") String newTimescamp
    ){
        Booking updatedBooking = bookingService.updateTimeBooking(id, newTimescamp);
        return CoreApiResponse.success(updatedBooking, "Update booking id: " + id + " successfully!");
    }

    @PutMapping("{id}/tracking-number")
    public CoreApiResponse<Booking> updateTrackingNumber(
            @Valid @PathVariable Long id,
            @Valid @RequestParam("trackingNumber") String trackingNumber
    ){
        Booking updatedBooking = bookingService.updateTrackingNumber(id, trackingNumber);
        return CoreApiResponse.success(updatedBooking, "Update booking id: " + id + " successfully!");
    }

    @DeleteMapping("{id}")
    public CoreApiResponse<Booking> deleteBooking(
            @Valid @PathVariable Long id
    ){
        Booking deletedBooking = bookingService.deleteBooking(id);
        return CoreApiResponse.success(deletedBooking, "Delete booking id: " + id + " successfully!");
    }

//    @PutMapping("/test")
//    public CoreApiResponse<?> test(){
//        bookingService.automaticallySetBirdCategoryFromCancelledBooking();
//        return CoreApiResponse.success("Success");
//    }

    @GetMapping("/pay-unpaid-booking")
    public CoreApiResponse<PaymentRespone> payUnpaidBooking(
        @Valid @RequestParam("id") Long bookingId
    ) throws UnsupportedEncodingException {
        return CoreApiResponse.success(bookingService.payUnpaidDepositMoney(bookingId), "Successfully");
    }

    @GetMapping("/pay-total-booking")
    public CoreApiResponse<PaymentRespone> payTotalBooking(
            @Valid @RequestParam("id") Long bookingId
    ) throws UnsupportedEncodingException {
        return CoreApiResponse.success(bookingService.payTotalMoney(bookingId), "Successfully");
    }
}
