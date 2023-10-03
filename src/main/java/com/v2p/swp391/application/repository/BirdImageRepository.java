package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.BirdImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BirdImageRepository extends JpaRepository<BirdImage,Long> {
    List<BirdImage> findByBirdId(Long birdId);
    long countByBirdId(Long birdId);


}
