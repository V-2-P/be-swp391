package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.repository.BirdRepository;
import com.v2p.swp391.application.repository.BirdTypeRepository;
import com.v2p.swp391.application.repository.CategoryRepository;
import com.v2p.swp391.application.response.BirdResponse;
import com.v2p.swp391.application.service.BirdService;
import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.utils.StringUtlis;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.v2p.swp391.application.mapper.BirdHttpMapper.INSTANCE;

@Service
@RequiredArgsConstructor
public class BirdServiceImpl implements BirdService {
    private final BirdRepository birdRepository;
    private final CategoryRepository categoryRepository;
    private final BirdTypeRepository birdTypeRepository;

    @Override
    public void createBird(Bird bird) {
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
        bird.setThumbnail(bird.getThumbnail());
        bird.setDescription(bird.getDescription());
        bird.setCategory(existingCategory);
        bird.setBirdType(existingBirdType);
        bird.setCompetitionAchievements(bird.getCompetitionAchievements());
        bird.setAge(bird.getAge());
        bird.setStatus(bird.isStatus());
        bird.setQuantity(bird.getQuantity());
        birdRepository.save(bird);
    }

    @Override
    public List<Bird> getAllBirds() {
        return birdRepository.findAll();
    }

    @Override
    public Bird getBirdById(long id) {
        return birdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bird", "id", id));
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
        return productsPage.map(INSTANCE::toResponse);
    }

    @Override
    public Bird updateBird(long id, Bird bird) {
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

        Bird existingBird = getBirdById(id);
        existingBird.setName(bird.getName());
        existingBird.setPrice(bird.getPrice());
        existingBird.setThumbnail(bird.getThumbnail());
        existingBird.setDescription(bird.getDescription());
        existingBird.setGender(bird.getGender());
        existingBird.setPurebredLevel(bird.getPurebredLevel());
        existingBird.setCompetitionAchievements(bird.getCompetitionAchievements());
        existingBird.setAge(bird.getAge());
        existingBird.setQuantity(bird.getQuantity());
        existingBird.setColor(bird.getColor());
        existingBird.setBirdType(existingBirdType);
        existingBird.setCategory(existingCategory);
        existingBird.setStatus(bird.isStatus());

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
        String oldThumbnail = existingBird.getThumbnail();

        if (oldThumbnail != null) {
            deleteThumbnail(oldThumbnail);
        }
        if(imageFile.getSize() == 0) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Please upload file image ");
        }
        if (!isImageFile(imageFile) || imageFile.getOriginalFilename() == null) {
            throw new AppException (HttpStatus.UNSUPPORTED_MEDIA_TYPE,"Invalid image format") ;
        }
        String originalFileName = imageFile.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        java.nio.file.Path uploadDir = Paths.get(Image.BIRD_IMAGE_PATH);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        java.nio.file.Path destination = Paths.get(uploadDir.toString(),uniqueFileName);
        Files.copy(imageFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        existingBird.setThumbnail(uniqueFileName);
        birdRepository.save(existingBird);
    }
    private void deleteThumbnail(String thumbnailFileName) throws IOException {
        java.nio.file.Path uploadDir = Paths.get(Image.BIRD_IMAGE_PATH);
        java.nio.file.Path thumbnailPath = uploadDir.resolve(thumbnailFileName);

        if (Files.exists(thumbnailPath)) {
            Files.delete(thumbnailPath);
        }
    }
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

}
