package com.v2p.swp391.application.service;


import com.v2p.swp391.application.model.Voucher;

import com.v2p.swp391.application.request.VoucherRequest;

import java.util.List;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    Voucher getVoucherById(Long id);
    List<Voucher> searchByCodeOrName(String searchText);
    List<Voucher> getAllVoucher();
    Voucher updateVoucher(Long id, VoucherRequest voucher);
    void deleteVoucher(Long id);

}
