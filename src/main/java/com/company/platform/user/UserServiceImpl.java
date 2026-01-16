package com.company.platform.user;

import com.company.platform.exception.UserNotFoundException;
import com.company.platform.role.Role;
import com.company.platform.role.RoleRepository;
import com.company.platform.user.dto.UserDto;
import com.company.platform.user.dto.UserListDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repo;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository repo,
                           RoleRepository roleRepository) {
        this.repo = repo;
        this.roleRepository = roleRepository;
    }

    // ================= USERNAME =================

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {

        log.info("Fetching user by username: {}", username);

        User user = repo.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UserNotFoundException("User not found: " + username);
                });

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsernameEntity(String username) {

        log.info("Fetching user entity by username: {}", username);

        return repo.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User entity not found: {}", username);
                    return new UserNotFoundException("User not found: " + username);
                });
    }

    // ================= EMAIL =================

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {

        log.info("Fetching user by email: {}", email);

        User user = repo.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmailEntity(String email) {

        log.info("Fetching user entity by email: {}", email);

        return repo.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User entity not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });
    }

    // ================= LIST =================

    @Override
    @Transactional(readOnly = true)
    public Page<UserListDto> getUsers(String keyword, Pageable pageable) {

        log.info("Fetching users list with keyword: {}", keyword);

        return repo.findByUsernameContainingIgnoreCase(keyword, pageable)
                .map(user -> new UserListDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()
                ));
    }

    // ================= ROLE ASSIGN =================

    @Override
    @Transactional
    public void assignRole(String email, String roleName) {

        log.info("Assigning role {} to user {}", roleName, email);

        User user = repo.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for role assignment: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> {
                    log.error("Role not found: {}", roleName);
                    return new RuntimeException("Role not found: " + roleName);
                });

        if (user.getRoles().contains(role)) {
            log.warn("User {} already has role {}", email, roleName);
            return;
        }

        user.getRoles().add(role);

        repo.save(user);

        log.info("Role {} assigned successfully to {}", roleName, email);
    }
}