package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.UseVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseVoucherRepository extends JpaRepository<UseVoucher,Long> {
    boolean existsByUser_IdAndVoucher_Id(Long userId, Long voucherId);

}
