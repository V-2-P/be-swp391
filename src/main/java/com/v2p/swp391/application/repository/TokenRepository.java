package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
