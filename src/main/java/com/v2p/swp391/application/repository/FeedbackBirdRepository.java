package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.model.FeedbackBird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackBirdRepository extends JpaRepository<FeedbackBird, Long> {
    List<FeedbackBird> findByBirdId(Long birdId);

    @Query("SELECT fb FROM FeedbackBird fb " +
            "INNER JOIN fb.bird b " +
            "WHERE b.birdType.id = :birdTypeId")
    List<FeedbackBird> findByBirdTypeId(@Param("birdTypeId") Long birdTypeId);
}


