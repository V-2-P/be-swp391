package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Boolean existsByEmail(String email);
    List<User> getUsersByRoleEntityId(Long roleId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.roleEntity.name = 'customer'")
    Long countCustomers();

    @Query("SELECT u FROM User u WHERE " +
            "(:roleId = 0 OR u.roleEntity.id = :roleId) " +
            "AND (:keyword = '' OR u.email LIKE %:keyword% OR u.fullName LIKE %:keyword% OR u.phoneNumber LIKE %:keyword%)"
    )
    List<User> searchUsers(@Param("roleId") Long roleId,
                           @Param("keyword") String phoneNumber);
}