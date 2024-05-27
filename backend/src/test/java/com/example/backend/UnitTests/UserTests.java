package com.example.backend.UnitTests;

import com.example.backend.Endpoints.Mappers.UserMapper;
import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Endpoints.dto.UserDetailDtoBuilder;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.AuthService;
import com.example.backend.security.JwtTokenizer;
import com.example.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class UserTests {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    private ApplicationUser applicationUser;

    @SpyBean
    private JwtTokenizer jwtTokenizer;

    @MockBean
    private AuthService authService;

    @BeforeEach
    public void cleanUp() throws ValidationException, ConflictException {
        applicationUser = userRepository.findById(1L).orElseThrow();
        when(authService.getUserFromToken()).thenReturn(applicationUser);
    }

    @Test
    @DisplayName("Register a Valid User - Success Scenario")
    void registerValidUser_shouldSuccessfullyRegisterTheUser() throws ValidationException, ConflictException {
        UserDetailDto userDetailDto = UserDetailDtoBuilder.builder()
                .firstName("Test")
                .lastName("Tester")
                .email("test.tester@example.com")
                .password("password")
                .build();

        String authToken = userService.register(userDetailDto);

        ApplicationUser registeredUser = userRepository.findUserByEmail("test.tester@example.com");


        assertAll("Registered User",
                () -> assertNotNull(registeredUser, "Registered user should not be null"),
                () -> assertEquals("Test", registeredUser.getFirstName(), "First name should be 'Test'"),
                () -> assertEquals("Tester", registeredUser.getLastName(), "Last name should be 'Tester'"),
                () -> assertEquals("test.tester@example.com", registeredUser.getEmail(), "Email should be 'test.tester@example.com'"),
                () -> assertTrue(passwordEncoder.matches("password", registeredUser.getPassword()), "Password should match 'password'"),
                () -> assertNotNull(authToken, "Auth token should not be null")
        );

    }




}
