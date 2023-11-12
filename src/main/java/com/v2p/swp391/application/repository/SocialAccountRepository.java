package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.SocialAccount;
import com.v2p.swp391.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findBySocialId(String id);

}
