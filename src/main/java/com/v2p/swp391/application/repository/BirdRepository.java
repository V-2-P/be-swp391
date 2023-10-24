package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Bird;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BirdRepository extends JpaRepository<Bird,Long> {
    boolean existsByName(String name);

    List<Bird> findByCategoryId(Long categoryId);

    List<Bird> findByBirdTypeId(Long typeId);
    Page<Bird> findAll(Pageable pageable);

    @Query("SELECT p FROM Bird p WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND(:typeId IS NULL OR : typeId = 0 OR p.birdType.id = :typeId)"+
            "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Bird> searchBirds
            (@Param("categoryId") Long categoryId,
             @Param("typeId") Long typeId,
             @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT p FROM Bird p LEFT JOIN FETCH p.birdImages WHERE p.id = :birdId")
    Optional<Bird> getDetailBird(@Param("birdId") Long birdId);

    @Query("SELECT od.bird FROM OrderDetail od " +
            "GROUP BY od.bird " +
            "ORDER BY SUM(od.numberOfProducts) DESC " +
            "LIMIT 4")
    List<Bird> findBestSeller();

    @Query("SELECT b FROM Bird b ORDER BY b.createdAt DESC LIMIT 20")
    List<Bird> findTop20();

    @Query("SELECT p FROM Bird p WHERE p.id IN :birdIds")
    List<Bird> findBirdsByIds(@Param("birdIds") List<Long> birdIds);

}
