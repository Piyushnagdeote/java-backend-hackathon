package com.company.platform.user;

import com.company.platform.user.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = repo.findByUsername(username).orElseThrow();
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }

    @Override
    public User getUserByUsernameEntity(String username) {
        return repo.findByUsername(username).orElseThrow();
    }
}
