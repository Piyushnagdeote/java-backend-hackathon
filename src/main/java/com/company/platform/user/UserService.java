package com.company.platform.user;

import com.company.platform.user.dto.UserDto;
import com.company.platform.user.dto.UserListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDto getUserByUsername(String username);

    User getUserByUsernameEntity(String username);

    UserDto getUserByEmail(String email);              // 🔥 ADD

    User getUserByEmailEntity(String email);           // 🔥 ADD

    void assignRole(String email, String roleName);


    Page<UserListDto> getUsers(String keyword, Pageable pageable);
}
