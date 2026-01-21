package com.company.platform.user;

import com.company.platform.api.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🔐 Current logged in user
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> me(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "User profile fetched",
                        userService.getUserByEmail(email)
                )
        );
    }


    // 🔐 Admin fetch any user by username
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable String username) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "User fetched",
                        userService.getUserByUsername(username)
                )
        );
    }
}
