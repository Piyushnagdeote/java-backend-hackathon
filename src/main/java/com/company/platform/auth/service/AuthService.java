package com.company.platform.auth.service;

import com.company.platform.auth.dto.RegisterRequest;
import com.company.platform.role.Role;
import com.company.platform.role.RoleRepository;
import com.company.platform.user.User;
import com.company.platform.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> register(RegisterRequest request) {

        System.out.println("REGISTER EMAIL = " + request.getEmail());
        System.out.println("REGISTER ROLE = " + request.getRole());

        if (userRepository.existsByEmail(request.getEmail())) {
            System.out.println("EMAIL ALREADY EXISTS IN DB");
            return ResponseEntity
                    .status(409)
                    .body(Map.of("message", "Email already exists"));
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

        System.out.println("ROLE FOUND = " + role.getName());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));
        user.setEnabled(true);

        userRepository.save(user);

        System.out.println("USER SAVED WITH EMAIL = " + user.getEmail());

        return ResponseEntity.ok(
                Map.of(
                        "message", "User registered successfully",
                        "email", request.getEmail()
                )
        );
    }


}
