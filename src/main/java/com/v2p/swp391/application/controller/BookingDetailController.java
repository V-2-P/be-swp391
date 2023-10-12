package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.BookingDetail;
import com.v2p.swp391.application.request.BookingDetailRequest;
import com.v2p.swp391.application.request.BookingRequest;
import com.v2p.swp391.application.service.BookingDetailService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.v2p.swp391.application.mapper.BookingDetailHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/bookingdetail")
@RequiredArgsConstructor
public class BookingDetailController {
    private final BookingDetailService bookingDetailService;

//    @PostMapping("")
//    public CoreApiResponse<BookingDetail> createBookingDetail(
//            @Valid @RequestBody BookingDetailRequest bookingDetailRequest,
//            BindingResult result
//    ){
//        BookingDetail bookingDetailRespone = bookingDetailService.createBookingDetail(INSTANCE.toModel(bookingDetailRequest));
//        return CoreApiResponse.success(bookingDetailRespone, "Insert booking sucessfully!");
//    }

    @GetMapping("/{id}")
    public  CoreApiResponse<BookingDetail> getBookingDetail(
        @PathVariable Long id
    ){
        BookingDetail bookingDetail = bookingDetailService.getBookingDetailById(id);
        return CoreApiResponse.success(bookingDetail);
    }

    @GetMapping("/booking/{id}")
    public  CoreApiResponse<List<BookingDetail>> getBookingDetailByBookingId(
            @PathVariable Long id
    ){
        List<BookingDetail> bookingDetails = bookingDetailService.getBookingDetailByBookingId(id);
        return CoreApiResponse.success(bookingDetails);
    }

    @PutMapping("/{id}/status")
    public CoreApiResponse<BookingDetail> updateBookingDetailStatus(
            @PathVariable Long id,
            @Valid @RequestBody String status
    ){
        BookingDetail updatedBookingDetail = bookingDetailService.updateBookingDetailStatus(id, status);
        return CoreApiResponse.success(updatedBookingDetail, "Successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<BookingDetail> deleteBookingDetailStatus(
            @PathVariable Long id
    ){
        BookingDetail deletedBookingDetail = bookingDetailService.deleteBookingDetail(id);
        return CoreApiResponse.success(deletedBookingDetail, "Successfully");
    }
}
