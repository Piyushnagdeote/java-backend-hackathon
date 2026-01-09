package com.company.platform.user;

import com.company.platform.role.Role;
import com.company.platform.role.RoleRepository;
import com.company.platform.user.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ REGISTER USER
    @Override
    public void registerUser(String username, String email, String rawPassword) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
        user.setRoles(Set.of(roleUser));

        userRepository.save(user);
    }

    // ✅ USED BY /api/user/profile
    @Override
    public UserDto getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRoles()
                .stream()
                .findFirst()
                .map(r -> r.getName())
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                role
        );
    }

    // ✅ REQUIRED FOR REFRESH TOKEN FLOW
    @Override
    public User getUserByUsernameEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
