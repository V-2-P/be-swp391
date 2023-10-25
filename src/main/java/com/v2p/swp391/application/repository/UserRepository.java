package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Boolean existsByEmail(String email);
    List<User> getUsersByRoleEntityId(Long roleId);

}