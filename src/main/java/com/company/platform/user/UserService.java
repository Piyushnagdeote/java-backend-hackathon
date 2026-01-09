package com.company.platform.user;

import com.company.platform.user.dto.UserDto;

public interface UserService {

    // ✅ Register user
    void registerUser(String username, String email, String rawPassword);

    // ✅ Used for /api/user/profile
    UserDto getUserByUsername(String username);

    // ✅ REQUIRED for refresh token logic
    User getUserByUsernameEntity(String username);
}
