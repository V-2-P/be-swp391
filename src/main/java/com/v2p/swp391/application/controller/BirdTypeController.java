package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.request.BirdTypeRequest;
import com.v2p.swp391.application.service.impl.BirdTypeServiceImpl;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.utils.StringUtlis;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.v2p.swp391.application.mapper.BirdTypeHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/birdtype")
@RequiredArgsConstructor
public class BirdTypeController {
    private final BirdTypeServiceImpl birdTypeService;
    @PostMapping("")
    public CoreApiResponse<BirdType> createBirdType(
            @Valid @RequestBody BirdTypeRequest birdTypeRequest
    ){
        birdTypeRequest.setName(StringUtlis.NameStandardlizing(birdTypeRequest.getName()));
        BirdType birdTypeResponse = birdTypeService.createBirdType(INSTANCE.toModel(birdTypeRequest));
        return CoreApiResponse.success(birdTypeResponse,"Insert bird type successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<BirdType>> getAllBirdTypes(){
        List<BirdType> birdTypes = birdTypeService.getAllBirdTypes();
        return CoreApiResponse.success(birdTypes);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<BirdType> getBirdTypeById(@Valid @PathVariable Long id){
        BirdType birdType = birdTypeService.getBirdTypeById(id);
        return CoreApiResponse.success(birdType);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<BirdType> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody BirdTypeRequest birdTypeRequest
    ){
        birdTypeRequest.setName(StringUtlis.NameStandardlizing(birdTypeRequest.getName()));
        BirdType updateBirdType = birdTypeService.updateBirdType(id, INSTANCE.toModel(birdTypeRequest));
        return CoreApiResponse.success(updateBirdType, "Update bird type successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<BirdType> deleteCategory(
            @PathVariable Long id
    ){
        BirdType deletedBirdType = birdTypeService.deleteBirdType(id);
        return CoreApiResponse.success("Delete bird type successfully with data: " + deletedBirdType);
    }
}
