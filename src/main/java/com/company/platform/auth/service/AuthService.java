package com.company.platform.auth.service;

import com.company.platform.security.JwtService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public Map<String, String> login(String username, List<String> roles) {

        String accessToken = jwtService.generateAccessToken(username, roles);
        String refreshToken = jwtService.generateRefreshToken(username);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }
}
