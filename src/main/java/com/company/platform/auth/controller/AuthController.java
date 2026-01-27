package com.company.platform.auth.controller;

import com.company.platform.auth.dto.LoginRequest;
import com.company.platform.auth.dto.RefreshRequest;
import com.company.platform.auth.dto.RegisterRequest;
import com.company.platform.auth.dto.TokenResponse;
import com.company.platform.auth.entity.RefreshToken;
import com.company.platform.auth.repository.RefreshTokenRepository;
import com.company.platform.auth.service.AuthService;
import com.company.platform.auth.service.RefreshTokenService;
import com.company.platform.entity.BlacklistedToken;
import com.company.platform.repository.BlacklistedTokenRepository;
import com.company.platform.security.JwtService;
import com.company.platform.user.User;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AuthService authService,
            RefreshTokenRepository refreshTokenRepository,
            RefreshTokenService refreshTokenService,
            BlacklistedTokenRepository blacklistedTokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authService = authService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = authService.getUserByEmail(request.getEmail());

        authService.unlockIfExpired(user);

        if (authService.isAccountLocked(user)) {
            return ResponseEntity.status(423)
                    .body(Map.of("message", "Account is locked. Try again later."));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            authService.resetFailedAttempts(user);

            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String accessToken = jwtService.generateAccessToken(user.getEmail(), roles);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken.getToken(),
                    "email", user.getEmail(),
                    "roles", roles
            ));

        } catch (Exception e) {
            authService.increaseFailedAttempts(user);
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid email or password"));
        }
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // ================= REFRESH =================
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

        RefreshToken token = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(token);

        User user = token.getUser();
        refreshTokenRepository.delete(token);

        List<String> roles = user.getRoles()
                .stream()
                .map(r -> r.getName())
                .toList();

        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), roles);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(
                new TokenResponse(newAccessToken, newRefreshToken.getToken())
        );
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<?> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Authorization header missing");
        }

        blacklistedTokenRepository.save(
                new BlacklistedToken(
                        authHeader.substring(7),
                        LocalDateTime.now().plusDays(1)
                )
        );

        return ResponseEntity.ok("Logged out successfully");
    }
}
