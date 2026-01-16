package com.company.platform.auth.repository;

import com.company.platform.auth.entity.RefreshToken;
import com.company.platform.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    // 🔥 AUTO CLEANUP SUPPORT
    void deleteByExpiryDateBefore(Instant time);
}
