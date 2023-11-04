package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.VoucherHttpMapper;
import com.v2p.swp391.application.model.Voucher;
import com.v2p.swp391.application.repository.VoucherRepository;
import com.v2p.swp391.application.request.VoucherRequest;
import com.v2p.swp391.application.service.VoucherService;
import com.v2p.swp391.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherHttpMapper voucherMapper;
    @Override
    public Voucher createVoucher(Voucher voucher) {
        if(voucherRepository.existsByCode(voucher.getCode())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Voucher code already exists");
        }
        voucher.setStatus(true);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher getVoucherById(Long id) {
        return voucherRepository.findById(id).
                orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find voucher with id: " + id));
    }

    @Override
    public List<Voucher> searchByCodeOrName(String searchText) {
        return voucherRepository.searchByCodeOrName(searchText);
    }

    @Override
    public List<Voucher> getAllVoucher() {

        List<Voucher> sortedVouchers = voucherRepository.findAll().stream()
                .sorted(Comparator.comparing(Voucher::getCreatedAt).reversed())
                .collect(Collectors.toList());
        return  sortedVouchers;
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

    @Scheduled(cron = "0 00 00 * * ?") // Chạy mỗi ngày vào lúc nửa đêm
    public void expireVouchers() {
        LocalDate today = LocalDate.now();
        List<Voucher> vouchersToExpire = voucherRepository.findByExpirationDateBeforeOrAmountLessThanEqualAndStatusIsTrue(today, 0);

        for (Voucher voucher : vouchersToExpire) {
            voucher.setStatus(false);
            // Cập nhật thông tin voucher nếu cần
            voucherRepository.save(voucher);
        }
    }
}
