package com.company.platform.user;

import com.company.platform.user.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByUsername_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setEmail("test@test.com");

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));

        UserDto dto = userService.getUserByUsername("test");

        assertEquals("test", dto.getUsername());
        assertEquals("test@test.com", dto.getEmail());
    }

    @Test
    void getUserByUsername_notFound() {
        when(userRepository.findByUsername("abc"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.getUserByUsername("abc"));
    }
}
