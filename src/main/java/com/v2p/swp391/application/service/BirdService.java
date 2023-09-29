package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.response.BirdResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BirdService {
    void createBird(Bird bird);

    List<Bird> getAllBirds();

    Bird getBirdById(long id);

    List<Bird> findByCategoryId(Long categoryId);

    List<Bird> findByTypeId(long typeId);

    Page<BirdResponse> getAllBirds(String keyword,
                                   Long categoryId, Long typeId, PageRequest pageRequest);

    Bird updateBird(long id, Bird bird);

    void deleteBird(long id);

    void uploadThumbnail(Long birdId, MultipartFile imageFile) throws IOException;
}


