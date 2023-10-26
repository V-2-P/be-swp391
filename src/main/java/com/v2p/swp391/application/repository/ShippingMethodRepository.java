package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod,Long> {

    boolean existsByName(String name);
}
