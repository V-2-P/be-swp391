package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.Bird;

import com.v2p.swp391.application.request.BirdRequest;
import com.v2p.swp391.application.response.BirdSearchResponse;
import com.v2p.swp391.application.response.BirdResponse;
import com.v2p.swp391.application.service.BirdService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.v2p.swp391.application.mapper.BirdHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/bird")
@RequiredArgsConstructor
public class BirdController {
    private final BirdService birdService;
    @PostMapping("")
    public CoreApiResponse<?> createBird(@Valid @ModelAttribute BirdRequest request,
                                         @RequestParam(name = "imageFile",required = false)  MultipartFile imageFile) throws IOException {
        Bird bird = INSTANCE.toModel(request);
        birdService.createBird(bird, imageFile);
        return CoreApiResponse.success(bird, "Create bird successfully");
    }
    @GetMapping("/{id}")
    public CoreApiResponse<BirdResponse> getBirdById(@Valid @PathVariable  Long id){
        Bird bird = birdService.getBirdById(id);
        return CoreApiResponse.success(INSTANCE.toResponse(bird));
    }

    @GetMapping("/all")
    public CoreApiResponse<?> getAllBird(){
        List<Bird> birds = birdService.getAllBirds();
        return CoreApiResponse.success(INSTANCE.toListResponses(birds));
    }

@GetMapping("")
public CoreApiResponse<?> getProducts(
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
        @RequestParam(defaultValue = "0", name = "type_id") Long typeId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "12") int limit
) {
    PageRequest pageRequest = PageRequest.of(
            page, limit,
            Sort.by("createdAt").descending()
    );

    Page<BirdResponse> productPage = birdService.getAllBirds(keyword, categoryId, typeId, pageRequest);
    int totalPages = productPage.getTotalPages();
    List<BirdResponse> birds = productPage.getContent();
    BirdSearchResponse birdSearchResponse = new BirdSearchResponse();
    birdSearchResponse.setBirds(birds);
    birdSearchResponse.setTotalPages(totalPages);
        return CoreApiResponse.success(birdSearchResponse);
}
    @GetMapping("/category/{category_id}")
    public CoreApiResponse<?> getBirdByCategoryId(@Valid @PathVariable("category_id") Long categoryId) {
        List<Bird> birds = birdService.findByCategoryId(categoryId);
        return CoreApiResponse.success(INSTANCE.toListResponses(birds));
    }

    @GetMapping("/birdtype/{type_id}")
    public CoreApiResponse<?> getBirdByTypeId(@Valid @PathVariable("type_id") Long typeId) {
        List<Bird> birds = birdService.findByTypeId(typeId);
        return CoreApiResponse.success(INSTANCE.toListResponses(birds));
    }



    @PutMapping("/{id}")
    public CoreApiResponse<?> updateBird(
            @PathVariable long id,
            @RequestBody BirdRequest birdRequest
    ) {
        Bird updatedBird = birdService.updateBird(id, birdRequest);
        return CoreApiResponse.success(updatedBird,"Update bird successfully");
    }
    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteCategory(@PathVariable Long id) {
        birdService.deleteBird(id);
        return CoreApiResponse.success("Delete category with id: " + id + " successfully");
    }

    @PostMapping("/uploadThumbnail/{birdId}")
    public CoreApiResponse<?> uploadThumbnail(
            @PathVariable Long birdId,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {
        birdService.uploadThumbnail(birdId,imageFile);
        return CoreApiResponse.success("Thumbnail uploaded successfully.");

    }





}
