package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.request.BirdRequest;
import com.v2p.swp391.application.response.BirdDetailResponse;
import com.v2p.swp391.application.response.BirdRecommendResponse;
import com.v2p.swp391.application.response.BirdResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BirdService {
    Bird createBird(Bird bird) ;

    List<Bird> getAllBirds();

    Bird getBirdById(long id);

    BirdDetailResponse getBirdDetail(Long birdId);

    List<Bird> findByCategoryId(Long categoryId);

    List<Bird> findByTypeId(long typeId);

    Page<Bird> getAllBirds(String keyword, Long categoryId, Long typeId, PageRequest pageRequest);

    Bird updateBird(long id, BirdRequest bird);

    void deleteBird(long id);

    void uploadThumbnail(Long birdId, MultipartFile imageFile) throws IOException;


    BirdRecommendResponse getRecommendBird();

    List<Bird> findBirdsByIds(List<Long> birdIds);





}


