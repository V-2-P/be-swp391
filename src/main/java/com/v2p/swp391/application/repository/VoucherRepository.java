package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long > {
    boolean existsByName(String name);

}
