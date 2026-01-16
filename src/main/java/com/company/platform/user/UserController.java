package com.company.platform.user;

import com.company.platform.user.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String userAccess() {
        return "USER ACCESS OK";
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {

        String username = authentication.getName();
        UserDto user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }
}
