package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.request.BirdRequest;
import com.v2p.swp391.application.service.BirdPairingService;
import com.v2p.swp391.application.service.BirdService;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class BirdParingServiceImpl implements BirdPairingService {
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final BirdRepository birdRepository;
    private final BirdPairingRepository birdPairingRepository;
    private final CategoryRepository categoryRepository;
    private final BirdService birdService;

    @Override
    public BirdPairing createBirdPairing(BirdPairing birdPairing) {
        BookingDetail existingBookingDetail = bookingDetailRepository
                .findById(birdPairing.getBookingDetail().getId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("Booking Detail", "id", birdPairing.getBookingDetail().getId()));

        birdPairing.setStatus(BirdPairingStatus.Egg);


        if(existingBookingDetail.getStatus().equals(BookingDetailStatus.In_Breeding_Progress)){
            existingBookingDetail.setStatus(BookingDetailStatus.Brooding);
            bookingDetailRepository.save(existingBookingDetail);
        }

        return birdPairingRepository.save(birdPairing);
    }

    @Override
    public BirdPairing getBirdPairingById(Long id) {
        BirdPairing existingBirdPairing = birdPairingRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("BirdPairing", "id", id));
        return existingBirdPairing;
    }

    @Override
    public BirdPairing updateNewBirdInformation(Long id, BirdRequest bird) {
        BirdPairing existingBirdPairing = birdPairingRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("BirdPairing", "id", id));

        Bird existingBird = birdRepository
                .findById(existingBirdPairing.getNewBird().getId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("Bird", "id", existingBirdPairing.getNewBird().getId()));

        birdService.updateBird(existingBird.getId(), bird);
        return birdPairingRepository.save(existingBirdPairing);
    }

    private boolean checkCompletedBrooding (BirdPairing birdPairing){
        List<BirdPairing> birdPairings = birdPairing.getBookingDetail().getBirdPairing();
        for(BirdPairing b: birdPairings){
            //Khac 2 trang thai Fledgling or Failed thi sai
            if(b.getStatus().equals(BirdPairingStatus.Egg))
                return false;
        }
        return true;
    }

    private boolean checkFailedBrooding (BirdPairing birdPairing){
        List<BirdPairing> birdPairings = birdPairing.getBookingDetail().getBirdPairing();
        for(BirdPairing b: birdPairings){
            //Khac 2 trang thai Fledgling or Failed thi sai
            if(!b.getStatus().equals(BirdPairingStatus.Failed))
                return false;
        }
        return true;
    }
    @Override
    public BirdPairing updateBirdPairingStatus(Long id, BirdPairingStatus status) {
        BirdPairing existingBirdPairing = birdPairingRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("BirdPairing", "id", id));
        BookingDetail existingBookingDetail = existingBirdPairing.getBookingDetail();
        if(status.equals(BirdPairingStatus.Fledgling)){
            Bird newBird = new Bird();
            newBird.setBirdType(existingBookingDetail.getBirdType());
            newBird.setCategory(categoryRepository.getReferenceById(1L));
            newBird.setStatus(false);
            newBird.setName("Customer userId: " + existingBookingDetail.getBooking().getUser().getId() + "'s bird");
            newBird = birdRepository.save(newBird);

            existingBirdPairing.setNewBird(newBird);
        }
        existingBirdPairing.setStatus(status);


        if(status.equals(BirdPairingStatus.Fledgling) || status.equals(BirdPairingStatus.Failed)){
            if(checkCompletedBrooding(existingBirdPairing)){
                BookingDetail bookingDetail = existingBirdPairing.getBookingDetail();
                bookingDetail.setStatus(BookingDetailStatus.Fledgling_All);
                bookingDetailRepository.save(bookingDetail);

                Booking booking = existingBirdPairing.getBookingDetail().getBooking();
                booking.setStatus(BookingStatus.Preparing);
                bookingRepository.save(booking);
            }

            if(checkFailedBrooding(existingBirdPairing)){
                existingBirdPairing.getBookingDetail().setStatus(BookingDetailStatus.Failed);
                bookingDetailRepository.save(existingBirdPairing.getBookingDetail());

                //Alert refund money or continue breeding?
            }
        }
        return birdPairingRepository.save(existingBirdPairing);
    }

    @Override
    public BirdPairing deleteBirdPairing(Long id) {
        BirdPairing existingBirdPairing = birdPairingRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("BirdPairing", "id", id));

        Bird existingBird = birdRepository
                .findById(existingBirdPairing.getNewBird().getId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("Bird", "id", existingBirdPairing.getNewBird().getId()));

        birdPairingRepository.deleteById(existingBirdPairing.getId());
        birdRepository.deleteById(existingBird.getId());

        return null;
    }
}
