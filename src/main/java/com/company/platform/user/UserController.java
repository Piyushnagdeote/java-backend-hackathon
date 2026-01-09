package com.company.platform.user;
import com.company.platform.user.dto.UserDto;

import com.company.platform.user.UserService; // your service to get user info
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String userAccess() {
        return "USER ACCESS OK";
    }

    // ✅ Add this
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {

        System.out.println("AUTH IN CONTROLLER = " + authentication);
        System.out.println("AUTHORITIES = " + authentication.getAuthorities());

        String username = authentication.getName();
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

}
