package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.model.BirdImage;
import com.v2p.swp391.application.repository.BirdRepository;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface BirdImageService {
    void createBirdImage(Long productId, List<MultipartFile> file) throws IOException;

    void deleteImage(long imageId);

    List<BirdImage> getAllImages(long birdId);

    BirdImage getImage(long imageId);


}
