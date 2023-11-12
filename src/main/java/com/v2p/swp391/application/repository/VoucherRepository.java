package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Voucher;
import com.v2p.swp391.application.model.VoucherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface VoucherRepository extends JpaRepository<Voucher, Long > {

    boolean existsByCode(String code);

    List<Voucher> findByStatus(VoucherStatus status);


    @Query("SELECT v FROM Voucher v WHERE (:search IS NULL OR :search = '' " +
            "OR v.code LIKE %:search% OR v.name LIKE %:search%) AND v.status = isActive")
    List<Voucher> searchByCodeOrName(@Param("search") String searchText);

    @Query("SELECT v FROM Voucher v WHERE v.expirationDate < :expirationDate OR v.amount <= :amount AND v.status = :status")
    List<Voucher> findExpiredOrOutOfStockVouchers(LocalDate expirationDate, int amount, VoucherStatus status);

    @Query("SELECT v FROM Voucher v WHERE v.id NOT IN (SELECT uv.voucher.id FROM UseVoucher uv WHERE uv.user.id = :userId)")
    List<Voucher> findVouchersNotUsedByUser(@Param("userId") Long userId);

}
