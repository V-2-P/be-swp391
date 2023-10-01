package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.mapper.BookingHttpMapper;
import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.request.BookingRequest;
import com.v2p.swp391.application.service.impl.BookingServiceImpl;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.v2p.swp391.application.mapper.BookingHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;

    @PostMapping("")
    public CoreApiResponse<?> createBooking(
            @Valid @RequestBody BookingRequest bookingRequest,
            BindingResult result
    ){
        Booking bookingRespone = bookingService.createBooking(INSTANCE.toModel(bookingRequest));
        return CoreApiResponse.success(bookingRespone, "Insert booking sucessfully!");
    }

    @GetMapping("")
    public CoreApiResponse<List<Booking>> getAllBookings (){
        List<Booking> bookings = bookingService.getAllBookings();
        return CoreApiResponse.success(bookings);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<Booking> getBookingById (
            @Valid @PathVariable Long id
    ){
        Booking booking = bookingService.getBookingById(id);
        return CoreApiResponse.success(booking);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<Booking> updateBooking(
            @Valid @PathVariable Long id,
            @Valid @RequestBody BookingRequest bookingRequest
    ){
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return CoreApiResponse.success(updatedBooking, "Update booking id: " + id + " successfully!");
    }

    @DeleteMapping("{id}")
    public CoreApiResponse<Booking> deleteBooking(
            @Valid @PathVariable Long id
    ){
        Booking deletedBooking = bookingService.deleteBooking(id);
        return CoreApiResponse.success(deletedBooking, "Delete booking id: " + id + " successfully!");
    }
}
