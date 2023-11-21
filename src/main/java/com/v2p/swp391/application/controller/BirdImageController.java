package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.BirdImage;

import com.v2p.swp391.application.response.BirdImageResponse;
import com.v2p.swp391.application.service.BirdImageService;
import com.v2p.swp391.common.api.CoreApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.v2p.swp391.application.mapper.BirdImageHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/birdimage")
@RequiredArgsConstructor
public class BirdImageController {
    private final BirdImageService birdImageService;

    @PostMapping("/{id}")
    public CoreApiResponse<List<BirdImage>>uploadImages(
            @PathVariable Long id,
            @RequestParam(name = "files") List<MultipartFile> images) throws IOException {
        List<BirdImage> birdimage = birdImageService.createBirdImage(id, images);
        return CoreApiResponse.success(birdimage,"Bird image uploaded successfully");
    }

    @GetMapping("/bird/{birdId}")
    public CoreApiResponse<List<BirdImageResponse>> getAllImageByBirdId(@PathVariable Long birdId) {
        List<BirdImage> images = birdImageService.getAllImages(birdId);
        return CoreApiResponse.success(INSTANCE.toListResponse(images));

    }


    @GetMapping("/{imageId}")
    public CoreApiResponse<BirdImageResponse> getImage(@PathVariable Long imageId) {
        BirdImage image = birdImageService.getImage(imageId);
        return CoreApiResponse.success(INSTANCE.toResponse(image));

    }

    @DeleteMapping("/{imageId}")
    public CoreApiResponse<?> deleteImage(@PathVariable Long imageId) {

        birdImageService.deleteImage(imageId);
        return CoreApiResponse.success("Delete image successfully");

    }


}
