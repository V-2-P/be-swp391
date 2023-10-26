package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface VoucherRepository extends JpaRepository<Voucher, Long > {

    boolean existsByCode(String code);

    @Query("SELECT v FROM Voucher v WHERE v.code LIKE %:search% OR v.name LIKE %:search%")
    List<Voucher> searchByCodeOrName(@Param("search") String searchText);
}
