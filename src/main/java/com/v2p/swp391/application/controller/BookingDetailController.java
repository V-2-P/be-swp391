package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.BookingDetail;
import com.v2p.swp391.application.model.BookingDetailStatus;
import com.v2p.swp391.application.service.BookingDetailService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.v2p.swp391.application.mapper.BookingDetailHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/bookingdetail")
@RequiredArgsConstructor
public class BookingDetailController {
    private final BookingDetailService bookingDetailService;

    @GetMapping("/{id}")
    public  CoreApiResponse<BookingDetail> getBookingDetail(
        @PathVariable Long id
    ){
        BookingDetail bookingDetail = bookingDetailService.getBookingDetailById(id);
        return CoreApiResponse.success(bookingDetail);
    }

    @GetMapping("/user/{id}")
    public  CoreApiResponse<BookingDetail> getBookingDetailForUser(
            @PathVariable Long id
    ){
        BookingDetail bookingDetail = bookingDetailService.getBookingDetailByIdForUser(id);
        return CoreApiResponse.success(bookingDetail);
    }


    @PutMapping("/{id}/status")
    public CoreApiResponse<BookingDetail> updateBookingDetailStatus(
            @PathVariable Long id,
            @RequestParam("status") BookingDetailStatus status
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
