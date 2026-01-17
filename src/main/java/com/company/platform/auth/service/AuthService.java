package com.company.platform.auth.service;

import com.company.platform.auth.dto.RegisterRequest;
import com.company.platform.role.Role;
import com.company.platform.role.RoleRepository;
import com.company.platform.user.User;
import com.company.platform.user.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_TIME_MINUTES = 15;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= REGISTER =================
    public ResponseEntity<?> register(RegisterRequest request) {

        log.info("Register attempt for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed. Email already exists: {}", request.getEmail());
            return ResponseEntity
                    .status(409)
                    .body(Map.of("message", "Email already exists"));
        }

        // 🔥 FIXED ROLE LOOKUP
        String roleName = request.getRole().trim().toUpperCase();

        log.info("Looking for role in DB: {}", roleName);

        Role role = roleRepository.findByNameIgnoreCase(roleName)
                .orElseThrow(() -> {
                    log.error("Role not found in DB: {}", roleName);
                    return new RuntimeException("Role not found: " + roleName);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));
        user.setEnabled(true);
        user.setFailedAttempts(0);
        user.setLockTime(null);

        userRepository.save(user);

        log.info("User registered successfully with email: {}", user.getEmail());

        return ResponseEntity.ok(
                Map.of(
                        "message", "User registered successfully",
                        "email", request.getEmail()
                )
        );
    }

    // ================= USER FETCH =================
    public User getUserByEmail(String email) {

        log.info("Fetching user by email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new RuntimeException("User not found with email: " + email);
                });
    }

    // ================= LOGIN FAILURE =================
    public void increaseFailedAttempts(User user) {

        int newFailAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(newFailAttempts);

        log.warn("Login failed for user {}. Failed attempts: {}",
                user.getEmail(), newFailAttempts);

        if (newFailAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setLockTime(LocalDateTime.now());
            log.error("Account locked for user: {}", user.getEmail());
        }

        userRepository.save(user);
    }

    // ================= LOGIN SUCCESS =================
    public void resetFailedAttempts(User user) {

        log.info("Resetting failed attempts for user: {}", user.getEmail());

        user.setFailedAttempts(0);
        user.setLockTime(null);
        userRepository.save(user);
    }

    // ================= LOCK CHECK =================
    public boolean isAccountLocked(User user) {

        if (user.getLockTime() == null) return false;

        LocalDateTime unlockTime = user.getLockTime().plusMinutes(LOCK_TIME_MINUTES);

        boolean locked = LocalDateTime.now().isBefore(unlockTime);

        if (locked) {
            log.warn("Account is currently locked for user: {}", user.getEmail());
        }

        return locked;
    }

    // ================= AUTO UNLOCK =================
    public void unlockIfExpired(User user) {

        if (user.getLockTime() == null) return;

        LocalDateTime unlockTime = user.getLockTime().plusMinutes(LOCK_TIME_MINUTES);

        if (LocalDateTime.now().isAfter(unlockTime)) {

            log.info("Unlocking account automatically for user: {}", user.getEmail());

            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
        }
    }
}
