package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.BirdHttpMapper;
import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.model.BirdImage;
import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.repository.BirdImageRepository;
import com.v2p.swp391.application.repository.BirdRepository;
import com.v2p.swp391.application.repository.BirdTypeRepository;
import com.v2p.swp391.application.repository.CategoryRepository;
import com.v2p.swp391.application.request.BirdRequest;
import com.v2p.swp391.application.response.BirdResponse;
import com.v2p.swp391.application.service.BirdService;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.utils.StringUtlis;
import com.v2p.swp391.utils.UploadImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BirdServiceImpl implements BirdService {
    private final BirdRepository birdRepository;
    private final CategoryRepository categoryRepository;
    private final BirdTypeRepository birdTypeRepository;
    private final BirdImageRepository birdImageRepository;
    private final BirdHttpMapper birdMapper;


    @Override
    public Bird createBird(Bird bird)  {
        Category existingCategory = categoryRepository
                .findById(bird.getCategory().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category", "id", bird.getCategory().getId()));
        BirdType existingBirdType = birdTypeRepository
                .findById(bird.getBirdType().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bird type", "id", bird.getBirdType().getId()));
        bird.setName((StringUtlis.NameStandardlizing(bird.getName())));
        bird.setPrice(bird.getPrice());
        bird.setDescription(bird.getDescription());
        bird.setCategory(existingCategory);
        bird.setBirdType(existingBirdType);
        bird.setCompetitionAchievements(bird.getCompetitionAchievements());
        bird.setAge(bird.getAge());
        bird.setStatus(bird.isStatus());
        bird.setQuantity(bird.getQuantity());
        bird.setBirdImages(bird.getBirdImages());
        bird.setThumbnail(bird.getThumbnail());
        return birdRepository.save(bird);

    }

    @Override
    public List<Bird> getAllBirds() {
        return birdRepository.findAll();
    }

    @Override
    public Bird getBirdById(long id) {
        return birdRepository.getDetailBird(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bird", "id", id));
    }

    @Override
    public Bird getDetailBirdById(long id) {
        Optional<Bird> optionalProduct = birdRepository.getDetailBird(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new ResourceNotFoundException("Bird", "id", id);
    }


    @Override
    public List<Bird> findByCategoryId(Long categoryId) {
        Category existingCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category", "id", categoryId));

        return birdRepository.findByCategoryId(categoryId);
    }


    @Override
    public List<Bird> findByTypeId(long typeId) {
        BirdType existingBirdType = birdTypeRepository
                .findById(typeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bird type", "id", typeId));
        return birdRepository.findByBirdTypeId(typeId);
    }

    @Override
    public Page<BirdResponse> getAllBirds(String keyword, Long categoryId, Long typeId, PageRequest pageRequest) {
        Page<Bird> productsPage;
        productsPage = birdRepository.searchBirds(categoryId, typeId, keyword, pageRequest);
        return productsPage.map(birdMapper::toResponse);
    }

    @Override
    public Bird updateBird(long id, BirdRequest request) {
        Bird existingBird = getBirdById(id);
        birdMapper.updateBirdFromRequest(request, existingBird);

        if (request.getTypeId() != null) {
            if (!existingBird.getBirdType().getId().equals(request.getTypeId())) {
                BirdType existingBirdType = birdTypeRepository
                        .findById(request.getTypeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Bird type", "id", request.getTypeId()));
            }
        }
        if (request.getCategoryId() != null) {
            if (!existingBird.getCategory().getId().equals(request.getCategoryId())) {
                Category existingCategory = categoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category", "id", request.getCategoryId()));

            }
        }


        return birdRepository.save(existingBird);
    }

    @Override
    public void deleteBird(long id) {
        Bird existingBird = getBirdById(id);
        birdRepository.deleteById(id);
    }

    @Override
    public void uploadThumbnail(Long birdId, MultipartFile imageFile) throws IOException {
        Bird existingBird = getBirdById(birdId);
        existingBird.setThumbnail(UploadImageUtils.storeFile(imageFile));
        birdRepository.save(existingBird);
    }

}
