package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.Bird;

import com.v2p.swp391.application.model.BirdImage;
import com.v2p.swp391.application.request.BirdRequest;
import com.v2p.swp391.application.response.BirdDetailResponse;
import com.v2p.swp391.application.response.BirdRecommendResponse;
import com.v2p.swp391.application.response.BirdSearchResponse;
import com.v2p.swp391.application.response.BirdResponse;
import com.v2p.swp391.application.service.BirdService;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.utils.UploadImageUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.v2p.swp391.application.mapper.BirdHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/birds")
@RequiredArgsConstructor
public class BirdController {
    private final BirdService birdService;

    @PostMapping("")
    public CoreApiResponse<Bird> createBird(@Valid @ModelAttribute BirdRequest request,
                                            @RequestParam(name = "imageThumbnail", required = false) MultipartFile imageFile,
                                            @RequestParam(name = "imagesFile", required = false) List<MultipartFile> images) throws IOException {
        Bird bird = INSTANCE.toModel(request);
        if (imageFile != null && !imageFile.isEmpty()) {
            bird.setThumbnail(UploadImageUtils.storeFile(imageFile, Image.BIRD_IMAGE_PATH));
        }

        if (images != null && !images.isEmpty()) {
            List<BirdImage> birdImagesList = new ArrayList<>();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    BirdImage birdImage = new BirdImage();
                    birdImage.setBird(bird);
                    birdImage.setImageUrl(UploadImageUtils.storeFile(image, Image.BIRD_IMAGE_PATH));
                    birdImagesList.add(birdImage);
                }
            }
            bird.setBirdImages(birdImagesList);
        }
        birdService.createBird(bird);
        return CoreApiResponse.success(bird, "Create bird successfully");
    }


    @GetMapping("/all")
    public CoreApiResponse<List<Bird>> getAllBird() {
        List<Bird> birds = birdService.getAllBirds();
        return CoreApiResponse.success(birds);
    }

    @GetMapping("")
    public CoreApiResponse<BirdSearchResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0", name = "type_id") Long typeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );

        Page<Bird> productPage = birdService.getAllBirds(keyword, categoryId, typeId, pageRequest);
        int totalPages = productPage.getTotalPages();
        List<Bird> birds = productPage.getContent();
        BirdSearchResponse birdSearchResponse = new BirdSearchResponse();
        birdSearchResponse.setBirds(birds);
        birdSearchResponse.setTotalPages(totalPages);
        return CoreApiResponse.success(birdSearchResponse);
    }

    @GetMapping("/category/{category_id}")
    public CoreApiResponse<List<BirdResponse>> getBirdByCategoryId(
            @Valid @PathVariable("category_id") Long categoryId) {
        List<Bird> birds = birdService.findByCategoryId(categoryId);
        return CoreApiResponse.success(INSTANCE.toListResponses(birds));
    }

    @GetMapping("/birdtype/{type_id}")
    public CoreApiResponse<List<BirdResponse>> getBirdByTypeId(
            @Valid @PathVariable("type_id") Long typeId) {
        List<Bird> birds = birdService.findByTypeId(typeId);
        return CoreApiResponse.success(INSTANCE.toListResponses(birds));
    }

    @GetMapping("/detail/{id}")
    public CoreApiResponse<BirdDetailResponse> getBirdDetailById(@PathVariable("id") Long birdId) {
        BirdDetailResponse response = birdService.getBirdDetail(birdId);
        return CoreApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<BirdDetailResponse> updateBird(
            @PathVariable long id,
            @RequestBody BirdRequest birdRequest) {
        Bird updatedBird = birdService.updateBird(id, birdRequest);
        return CoreApiResponse.success(INSTANCE.toDetail(updatedBird), "Update bird successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteBird(@PathVariable Long id) {
        birdService.deleteBird(id);
        return CoreApiResponse.success("Delete bird with id: " + id + " successfully");
    }

    @PostMapping("/uploadThumbnail/{birdId}")
    public CoreApiResponse<?> uploadThumbnail(
            @PathVariable Long birdId,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        birdService.uploadThumbnail(birdId, imageFile);
        return CoreApiResponse.success("Thumbnail uploaded successfully.");

    }

    @GetMapping("/recommendation")
    public CoreApiResponse<BirdRecommendResponse> getRecommendBird() {
        BirdRecommendResponse recommendation = birdService.getRecommendBird();
        return CoreApiResponse.success(recommendation);
    }

    @GetMapping("/by-ids")
    public CoreApiResponse<?> getProductsByIds(@RequestParam("ids") String ids) {
        List<Long> birdIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Bird> birds = birdService.findBirdsByIds(birdIds);
        return CoreApiResponse.success(birds);
    }
}
