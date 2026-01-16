package com.company.platform.admin;

import com.company.platform.api.ResponseUtil;
import com.company.platform.user.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // 🔐 ADMIN test endpoint
    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminAccess() {
        return ResponseEntity.ok(
                ResponseUtil.success("ADMIN ACCESS OK", null)
        );
    }

    // 🔐 ADMIN list users
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "") String keyword,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Users fetched successfully",
                        userService.getUsers(keyword, pageable)
                )
        );
    }

    // 🔐 ADMIN get user by email
    @GetMapping("/users/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "User fetched successfully",
                        userService.getUserByEmail(email)
                )
        );
    }

    // 🔐 ADMIN assign role to user
    @PutMapping("/users/{email}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRole(
            @PathVariable String email,
            @RequestBody Map<String, String> body
    ) {

        userService.assignRole(email, body.get("role"));

        return ResponseEntity.ok(
                ResponseUtil.success("Role assigned successfully", null)
        );
    }
}
