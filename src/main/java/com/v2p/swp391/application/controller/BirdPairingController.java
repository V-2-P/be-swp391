package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.mapper.BirdPairingHttpMapper;
import com.v2p.swp391.application.model.BirdPairing;
import com.v2p.swp391.application.model.BirdPairingStatus;
import com.v2p.swp391.application.request.BirdParingRequest;
import com.v2p.swp391.application.request.BirdRequest;
import com.v2p.swp391.application.service.impl.BirdParingServiceImpl;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.version.v1}/birdparing")
@RequiredArgsConstructor
public class BirdPairingController {
    private final BirdParingServiceImpl birdParingService;

    @PostMapping("")
    public CoreApiResponse<BirdPairing> createBirdPairing(
            @Valid @RequestBody BirdParingRequest birdParingRequest
    ){
        BirdPairing newBirdPairing = birdParingService.createBirdPairing(
                BirdPairingHttpMapper.INSTANCE.toModel(birdParingRequest));
        return CoreApiResponse.success(newBirdPairing);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<BirdPairing> getBirdPairing(
            @Valid @PathVariable Long id
    ){
        BirdPairing birdPairing = birdParingService.getBirdPairingById(id);
        return CoreApiResponse.success(birdPairing, "Successful");
    }

    @PutMapping("/{id}")
    public CoreApiResponse<BirdPairing> updateBirdInBirdPairing(
            @Valid @PathVariable Long id,
            @RequestBody BirdRequest birdRequest
    ){
        BirdPairing birdPairing = birdParingService.updateNewBirdInformation(id, birdRequest);
        return CoreApiResponse.success(birdPairing);
    }

    @PutMapping("/{id}/status")
    public CoreApiResponse<BirdPairing> updateStatusInBirdInBooking(
            @Valid @PathVariable Long id,
            @Valid @RequestParam("status") BirdPairingStatus status
    ){
        BirdPairing birdPairing = birdParingService.updateBirdPairingStatus(id, status);
        return CoreApiResponse.success(birdPairing);
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<BirdPairing> deleteBookingId(
            @Valid @PathVariable Long id
    ){
        BirdPairing birdPairing = birdParingService.deleteBirdPairing(id);
        return CoreApiResponse.success(birdPairing);
    }
}
