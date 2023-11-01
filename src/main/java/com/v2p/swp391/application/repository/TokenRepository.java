package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByName(String name);

}
