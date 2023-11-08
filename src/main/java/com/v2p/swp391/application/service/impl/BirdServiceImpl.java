package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.BirdHttpMapper;
import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.request.BirdRequest;
import com.v2p.swp391.application.response.BirdDetailResponse;
import com.v2p.swp391.application.response.BirdRecommendResponse;
import com.v2p.swp391.application.response.BirdResponse;
import com.v2p.swp391.application.service.BirdService;
import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.security.UserPrincipal;
import com.v2p.swp391.utils.StringUtlis;
import com.v2p.swp391.utils.UploadImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BirdServiceImpl implements BirdService {
    private final BirdRepository birdRepository;
    private final CategoryRepository categoryRepository;
    private final BirdTypeRepository birdTypeRepository;
    private final FeedbackBirdRepository feedbackBirdRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final BirdHttpMapper birdMapper;



    @Override
    public Bird createBird(Bird bird) {
        Category existingCategory = categoryRepository
                .findById(bird.getCategory().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category", "id", bird.getCategory().getId()));
        BirdType existingBirdType = birdTypeRepository
                .findById(bird.getBirdType().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bird type", "id", bird.getBirdType().getId()));
        bird.setName((StringUtlis.NameStandardlizing(bird.getName())));
        bird.setCategory(existingCategory);
        bird.setBirdType(existingBirdType);
        return birdRepository.save(bird);

    }

    @Override
    public List<Bird> getAllBirds() {
        return birdRepository.findAll();
    }

    @Override
    public Bird getBirdById(long id) {
        return birdRepository.getDetailBird(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bird", "id", id));
    }

    public Bird getDetailBirdById(long id) {
        return birdRepository.getDetailBird(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bird", "id", id));
    }
    @Override
    public BirdDetailResponse getBirdDetail(Long birdId) {
        Bird bird = getDetailBirdById(birdId);
        BirdDetailResponse response = birdMapper.toDetail(bird);
        response.setTotalRating(totalRatingByBirdId(birdId));
        response.setCountRating(countFeedback(birdId));
        response.setSold(totalSoldOut(birdId));
        return response;
    }


    @Override
    public List<Bird> findByCategoryId(Long categoryId) {
        Category existingCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category", "id", categoryId));

        return birdRepository.findByCategoryId(categoryId);
    }


    @Override
    public List<Bird> findByTypeId(long typeId) {
        BirdType existingBirdType = birdTypeRepository
                .findById(typeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bird type", "id", typeId));
        return birdRepository.findByBirdTypeId(typeId);
    }

    @Override
    public Page<Bird> getAllBirds(String keyword, Long categoryId, Long typeId,Float minPrice, Float maxPrice, PageRequest pageRequest) {
        Page<Bird> productsPage;
        productsPage = birdRepository.searchBirds(categoryId, typeId, keyword,minPrice, maxPrice, pageRequest);
        return productsPage;
    }

    @Override
    public Bird updateBird(long id, BirdRequest request) {
        Bird existingBird = getBirdById(id);
        birdMapper.updateBirdFromRequest(request, existingBird);


        if (request.getTypeId() != null && !existingBird.getBirdType().getId().equals(request.getTypeId())) {
            BirdType existingBirdType = birdTypeRepository
                    .findById(request.getTypeId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Bird type", "id", request.getTypeId()));
            existingBird.setBirdType(existingBirdType);
        }


        if (request.getCategoryId() != null && !existingBird.getCategory().getId().equals(request.getCategoryId())) {
            Category existingCategory = categoryRepository
                    .findById(request.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Category", "id", request.getCategoryId()));
            existingBird.setCategory(existingCategory);
        }


        return birdRepository.save(existingBird);
    }

    @Override
    public void deleteBird(long id) {
        Bird existingBird = getBirdById(id);
        birdRepository.deleteById(id);
    }

    @Override
    public void uploadThumbnail(Long birdId, MultipartFile imageFile) throws IOException {
        Bird existingBird = getBirdById(birdId);
        existingBird.setThumbnail(UploadImageUtils.storeFile(imageFile, Image.BIRD_IMAGE_PATH));
        birdRepository.save(existingBird);
    }
    public BirdRecommendResponse getRecommendBird() {
        List<Bird> bestSellers = birdRepository.findBestSeller();
        List<Bird> top20 = birdRepository.findTop20();
        return new BirdRecommendResponse(birdMapper.toListResponses(bestSellers)
                                        , birdMapper.toListResponses(top20));
    }

    @Override
    public List<Bird> findBirdsByIds(List<Long> birdIds) {
        return birdRepository.findBirdsByIds(birdIds);
    }

    public double totalRatingByBirdId(Long birdId) {
        List<FeedbackBird> feedbackBirds = feedbackBirdRepository.findByBirdId(birdId);
        double totalRating = 0.0;
        int numberOfRatings = feedbackBirds.size();

        if (numberOfRatings > 0) {
            for (FeedbackBird feedbackBird : feedbackBirds) {
                totalRating += feedbackBird.getRating();
            }
            double averageRating = totalRating / numberOfRatings;
            return averageRating;
        } else {
            return 0.0;
        }
    }

    public int countFeedback(Long birdId) {
        Integer feedbackCount = feedbackBirdRepository.countByBirdId(birdId);

        if (feedbackCount != null) {
            return feedbackCount.intValue();
        } else {
            return 0;
        }
    }

    public int totalSoldOut(Long birdId) {
        Integer soldBirds = orderDetailRepository.countSoldBirds(birdId);

        if (soldBirds != null) {
            return soldBirds.intValue();
        } else {
            return 0;
        }
    }

    public List<Bird> recommend( Bird recentViewed, int K) {
        BirdType recentViewedBirdType = recentViewed.getBirdType();
        Category recentViewedCategory = recentViewed.getCategory();

        Map<Bird, Integer> freqMap = new HashMap();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.getUser();


            if (user != null) {
                Sort sortByCreatedAtDesc = Sort.by(Sort.Order.desc("createdAt"));
                List<Order> userOrders = orderRepository.findByUserId(user.getId(),sortByCreatedAtDesc);

                for (Order order : userOrders) {
                    for (OrderDetail detail : order.getOrderDetails()) {
                        Bird bird = detail.getBird();
                        BirdType birdType = bird.getBirdType();
                        Category category = bird.getCategory();

                        if (birdType.getId().equals(recentViewedBirdType.getId()) || category.getId().equals(recentViewedCategory.getId())) {
                            freqMap.put(bird, freqMap.getOrDefault(bird, 0) + 1);
                        }
                    }
                }
            }
        }

        // Lấy tất cả loài chim cùng loại với recentViewed
        List<Bird> birdsOfSameType = birdRepository.findByBirdTypeId(recentViewedBirdType.getId());

        // Lấy tất cả loài chim cùng danh mục với recentViewed
        List<Bird> birdsOfSameCategory = birdRepository.findByCategoryId(recentViewedCategory.getId());

        // Tổng hợp loài chim từ cùng loại và cùng danh mục
        List<Bird> allSimilarBirds = new ArrayList<>();
        allSimilarBirds.addAll(birdsOfSameType);
        allSimilarBirds.addAll(birdsOfSameCategory);

        for (Bird bird : allSimilarBirds) {
            freqMap.put(bird, freqMap.getOrDefault(bird, 0) + 1);
        }

        List<Bird> recommendations = freqMap.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(K)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendations;
    }
    @Override
    public List<Bird> findBirdsInPriceRange(Float minPrice, Float maxPrice) {
        return birdRepository.findBirdsInPriceRange(minPrice, maxPrice);
    }
}
