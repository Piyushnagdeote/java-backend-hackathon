package com.company.platform.auth.controller;

import com.company.platform.auth.dto.LoginRequest;
import com.company.platform.auth.dto.RegisterRequest;
import com.company.platform.security.JwtService;
import com.company.platform.user.UserService;
import com.company.platform.user.User;
import com.company.platform.role.Role;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    // REGISTER
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );
        return Map.of("message", "User registered successfully");
    }

    // LOGIN
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList();

        String accessToken = jwtService.generateAccessToken(
                request.getUsername(),
                roles
        );

        String refreshToken = jwtService.generateRefreshToken(
                request.getUsername()
        );

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }
}

