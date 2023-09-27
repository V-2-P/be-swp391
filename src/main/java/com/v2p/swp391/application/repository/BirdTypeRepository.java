package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.BirdType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirdTypeRepository extends JpaRepository<BirdType, Long> {
    boolean existsByName(String name);
}
