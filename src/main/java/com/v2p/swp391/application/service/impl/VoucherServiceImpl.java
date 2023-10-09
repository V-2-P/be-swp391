package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.VoucherHttpMapper;
import com.v2p.swp391.application.model.Voucher;
import com.v2p.swp391.application.repository.VoucherRepository;
import com.v2p.swp391.application.request.VoucherRequest;
import com.v2p.swp391.application.service.VoucherService;
import com.v2p.swp391.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherHttpMapper voucherMapper;
    @Override
    public Voucher createVoucher(Voucher voucher) {
        if(voucherRepository.existsByName(voucher.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Voucher name already exists");
        }

        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher getVoucherById(Long id) {
        return voucherRepository.findById(id).
                orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find voucher with id: " + id));
    }

    @Override
    public List<Voucher> getAllVoucher() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher updateVoucher(Long id, VoucherRequest voucher) {
        Voucher existingVoucher = getVoucherById(id);
        voucherMapper.updateVoucherFromRequest(voucher, existingVoucher);
        return voucherRepository.save(existingVoucher);
    }

    @Override
    public void deleteVoucher(Long id) {
        Voucher existingVoucher = getVoucherById(id);
        voucherRepository.deleteById(id);

    }
}
