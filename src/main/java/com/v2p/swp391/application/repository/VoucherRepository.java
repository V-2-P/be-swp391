package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface VoucherRepository extends JpaRepository<Voucher, Long > {
    boolean existsByCode(String code);


}
