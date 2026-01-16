package com.company.platform.auth.scheduler;

import com.company.platform.auth.repository.RefreshTokenRepository;
import com.company.platform.repository.BlacklistedTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class TokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public TokenCleanupScheduler(RefreshTokenRepository refreshTokenRepository,
                                 BlacklistedTokenRepository blacklistedTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    // Runs every 1 hour
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredTokens() {

        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());

        blacklistedTokenRepository.deleteByExpiryBefore(LocalDateTime.now());

        System.out.println("Expired tokens cleaned up");
    }
}
