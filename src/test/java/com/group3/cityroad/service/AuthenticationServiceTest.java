package com.group3.cityroad.service;

import com.group3.cityroad.entity.Resident;
import com.group3.cityroad.entity.User;
import com.group3.cityroad.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User mockResident;

    @BeforeEach
    void setUp() {
        mockResident = new Resident();
        mockResident.setUsername("resident1");
        mockResident.setPasswordHash("encoded_password");
        mockResident.setName("John Doe");
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByUsername("resident1")).thenReturn(Optional.of(mockResident));
        when(passwordEncoder.matches("raw_password", "encoded_password")).thenReturn(true);

        User result = authenticationService.login("resident1", "raw_password");

        assertNotNull(result, "User should be returned on successful match");
        assertEquals("resident1", result.getUsername());
    }

    @Test
    void testLoginFailure_WrongPassword() {
        when(userRepository.findByUsername("resident1")).thenReturn(Optional.of(mockResident));
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

        com.group3.cityroad.exception.InvalidCredentialsException exception = assertThrows(
            com.group3.cityroad.exception.InvalidCredentialsException.class,
            () -> authenticationService.login("resident1", "wrong_password")
        );

        assertTrue(exception.getMessage().contains("Incorrect password"));
    }

    @Test
    void testLoginFailure_UserNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        com.group3.cityroad.exception.InvalidCredentialsException exception = assertThrows(
            com.group3.cityroad.exception.InvalidCredentialsException.class,
            () -> authenticationService.login("ghost", "raw_password")
        );

        assertTrue(exception.getMessage().contains("Username not found"));
        verify(passwordEncoder, never()).matches(anyString(), anyString()); // Encoder shouldn't be called
    }
}
