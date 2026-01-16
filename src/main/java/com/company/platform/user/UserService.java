package com.company.platform.user;

import com.company.platform.user.dto.UserDto;

public interface UserService {

    UserDto getUserByUsername(String username);

    User getUserByUsernameEntity(String username);
}
