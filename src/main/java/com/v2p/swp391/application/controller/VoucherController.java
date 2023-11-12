package com.v2p.swp391.application.controller;


import com.v2p.swp391.application.model.Voucher;

import com.v2p.swp391.application.request.VoucherRequest;
import com.v2p.swp391.application.service.VoucherService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.v2p.swp391.application.mapper.VoucherHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/voucher")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class VoucherController {
    private final VoucherService voucherService;
    @PostMapping("")
    public CoreApiResponse<Voucher> createVoucher(
            @Valid @RequestBody VoucherRequest voucherRequest
    ){
        Voucher voucher = voucherService.createVoucher(INSTANCE.toModel(voucherRequest));
        return CoreApiResponse.success(voucher,"Insert voucher successfully");
    }

    @GetMapping("/{id}")
    public CoreApiResponse<Voucher> getVoucherById(@Valid @PathVariable Long id){
        Voucher voucher = voucherService.getVoucherById(id);
        return CoreApiResponse.success(voucher);
    }
    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public CoreApiResponse<List<Voucher>> getVoucherForCustomer() {
        List<Voucher> voucherForUser = voucherService.getVoucherForCustomer();
        return CoreApiResponse.success(voucherForUser);
    }

    @GetMapping("/search")
    public CoreApiResponse<List<Voucher>> searchVouchers(@RequestParam(defaultValue = "", name = "search") String search) {
        List<Voucher> vouchers = voucherService.searchByCodeOrName(search);
        return CoreApiResponse.success(vouchers);
    }
    @GetMapping("customer/search")
    public CoreApiResponse<List<Voucher>> searchVoucherForCustomer(@RequestParam(defaultValue = "", name = "search") String search) {
        List<Voucher> vouchers = voucherService.searchForCustomer(search);
        return CoreApiResponse.success(vouchers);
    }

    @GetMapping("")
    public CoreApiResponse<List<Voucher>> getAllVouchers(){
        List<Voucher> birdTypes = voucherService.getAllVoucher();
        return CoreApiResponse.success(birdTypes);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<Voucher> updateVoucher(
            @PathVariable Long id,
            @RequestBody VoucherRequest request
    ){
        Voucher updateVoucher= voucherService.updateVoucher(id, request);
        return CoreApiResponse.success(updateVoucher, "Update voucher successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteVoucher(
            @PathVariable Long id
    ){
        voucherService.cancelVoucher(id);
        return CoreApiResponse.success("Cancel voucher with id: " + id + " successfully");
    }
}
