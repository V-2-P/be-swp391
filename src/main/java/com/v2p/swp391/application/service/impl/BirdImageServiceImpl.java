package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.model.BirdImage;
import com.v2p.swp391.application.repository.BirdImageRepository;
import com.v2p.swp391.application.repository.BirdRepository;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.utils.UploadImageUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BirdImageServiceImpl implements com.v2p.swp391.application.service.BirdImageService {
    private final BirdRepository birdRepository;
    private final BirdImageRepository birdImageRepository;
    @Override
    public void createBirdImage(Long birdId, List<MultipartFile> images) throws IOException {
        Bird existingProduct = birdRepository
                .findById(birdId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bird","id",birdId));
        long existingImages = birdImageRepository.countByBirdId(birdId);
        long  remaining = BirdImage.MAXIMUM_IMAGES_PER_BIRD - existingImages;
        if (images.size() > remaining) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Maximum image is: " + BirdImage.MAXIMUM_IMAGES_PER_BIRD+
                    ". Please remove image before upload");
        }
        if (images.size() > BirdImage.MAXIMUM_IMAGES_PER_BIRD) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Maximum image is: " + BirdImage.MAXIMUM_IMAGES_PER_BIRD);
        }

        for(MultipartFile image : images) {
            if(!image.isEmpty()) {
                BirdImage birdImage = new BirdImage();
                birdImage.setBird(existingProduct);
                birdImage.setImageUrl(UploadImageUtils.storeFile(image));
                birdImageRepository.save(birdImage);
            }
        }
    }

    @Override
    public void deleteImage(long imageId) {
        BirdImage image = birdImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
        birdImageRepository.delete(image);
    }

    @Override
    public List<BirdImage> getAllImages(long birdId) {
        return birdImageRepository.findByBirdId(birdId);
    }

    @Override
    public BirdImage getImage(long imageId) {
        return birdImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
    }
}
