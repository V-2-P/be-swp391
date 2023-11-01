package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.BirdType;

import java.util.List;

public interface BirdTypeService {
    BirdType createBirdType(BirdType birdType);
    BirdType getBirdTypeById(Long id);
    List<BirdType> getAllBirdTypes();
    BirdType updateBirdType(Long birdTypeId, BirdType birdType);
    BirdType deleteBirdType(Long id);
}
