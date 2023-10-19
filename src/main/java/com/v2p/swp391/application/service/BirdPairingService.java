package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.model.BirdPairing;
import com.v2p.swp391.application.model.BirdPairingStatus;
import com.v2p.swp391.application.request.BirdRequest;

import java.util.List;

public interface BirdPairingService {
    public BirdPairing createBirdPairing(BirdPairing birdPairing);
    public BirdPairing getBirdPairingById(Long id);
    public BirdPairing updateNewBirdInformation(Long id, BirdRequest bird);
    public BirdPairing updateBirdPairingStatus(Long id, BirdPairingStatus status);
    public BirdPairing deleteBirdPairing(Long id);
}
