package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.repository.BirdTypeRepository;
import com.v2p.swp391.application.service.BirdTypeService;
import com.v2p.swp391.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.v2p.swp391.utils.StringUtlis;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BirdTypeServiceImpl implements BirdTypeService {
    private final BirdTypeRepository birdTypeRepository;
    @Override
    public BirdType createBirdType(BirdType birdType) {
        birdType.setName(StringUtlis.NameStandardlizing(birdType.getName()));
        if(birdTypeRepository.existsByName(birdType.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Bird type name already exists");
        }
        return birdTypeRepository.save(birdType);
    }

    @Override
    public BirdType getBirdTypeById(Long id) {
        return birdTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find bird type with id: " + id));
    }

    @Override
    public List<BirdType> getAllBirdTypes() {
        return birdTypeRepository.findAll();
    }

    @Override
    public BirdType updateBirdType(Long birdTypeId, BirdType birdType) {
        birdType.setName(StringUtlis.NameStandardlizing(birdType.getName()));
        if(birdTypeRepository.existsByName(birdType.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Bird type name already exists");
        }
        BirdType existingBirdType = getBirdTypeById(birdTypeId);
        existingBirdType.setName(birdType.getName());
        return birdTypeRepository.save(existingBirdType);
    }

    @Override
    public BirdType deleteBirdType(Long id) {
        BirdType existingBirdType = getBirdTypeById(id);
        birdTypeRepository.deleteById(id);
        return existingBirdType;
    }
}
