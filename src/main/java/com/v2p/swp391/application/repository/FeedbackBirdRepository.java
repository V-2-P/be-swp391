package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.FeedbackBird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackBirdRepository extends JpaRepository<FeedbackBird, Long> {
    List<FeedbackBird> findByBirdId(Long birdId);
}
