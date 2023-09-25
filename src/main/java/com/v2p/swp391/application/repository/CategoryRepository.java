package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String name);

}
