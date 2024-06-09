package com.example.backend.UnitTests;

import com.example.backend.Endpoints.Mappers.UserMapper;
import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Endpoints.dto.UserDetailDtoBuilder;
import com.example.backend.Endpoints.dto.UserLoginDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.baseTest.TestDataGenerator;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class UserTests {

    @Autowired
    TestDataGenerator testDataGenerator;

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
        testDataGenerator.cleanUp();
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

    @Test
    @DisplayName("Login a Valid User - Success Scenario")
    void loginValidUser_shouldSuccessfullyLoginTheUser() throws ValidationException, ConflictException {

        UserDetailDto userDetailDto = UserDetailDtoBuilder.builder()
                .firstName("Test")
                .lastName("Tester")
                .email("test.tester@example.com")
                .password("password")
                .build();

        userService.register(userDetailDto);

        UserLoginDto userLoginDto = new UserLoginDto(userDetailDto.email(), userDetailDto.password());
        String authToken = userService.login(userLoginDto);

        assertNotNull(authToken);
    }

    @Test
    @DisplayName("Register User Without First Name - Should Throw ValidationException")
    void registerUserWithoutFirstName_shouldThrowValidationException() {
        UserDetailDto userWithoutFirstName = UserDetailDtoBuilder.builder()
                .lastName("Tester")
                .email("test.tester@example.com")
                .password("password")
                .build();

        assertThrows(ValidationException.class, () -> {
            userService.register(userWithoutFirstName);
        });


    }

    @Test
    @DisplayName("Register User With Invalid Email - Should Throw ValidationException")
    void registerUserWithInvalidEmail_shouldThrowValidationException() {
        UserDetailDto userWithInvalidEmail = UserDetailDtoBuilder.builder()
                .firstName("Test")
                .lastName("Tester")
                .email("invalid-email")
                .password("password")
                .build();
        assertThrows(ValidationException.class, () -> {
            userService.register(userWithInvalidEmail);
        });


    }

    @Test
    @DisplayName("Register User With Short Password - Should Throw ValidationException")
    void registerUserWithShortPassword_shouldThrowValidationException() {
        UserDetailDto userWithShortPassword = UserDetailDtoBuilder.builder()
                .firstName("Test")
                .lastName("Tester")
                .email("test.tester@example.com")
                .password("short")
                .build();

        assertThrows(ValidationException.class, () -> {
            userService.register(userWithShortPassword);
        });

        ;
    }

    @Test
    @DisplayName("Register User Without Email - Should Throw ValidationException")
    void registerUserWithoutEmail_shouldThrowValidationException() {
        UserDetailDto userWithoutEmail = UserDetailDtoBuilder.builder()
                .firstName("Test")
                .lastName("Tester")
                .password("password")
                .build();

        assertThrows(ValidationException.class, () -> {
            userService.register(userWithoutEmail);
        });


    }

    @Test
    @DisplayName("Register User Without Password - Should Throw ValidationException")
    void registerUserWithoutPassword_shouldThrowValidationException() {
        UserDetailDto userWithoutPassword = UserDetailDtoBuilder.builder()
                .firstName("Test")
                .lastName("Tester")
                .email("test.tester@example.com")
                .build();

        assertThrows(ValidationException.class, () -> {
            userService.register(userWithoutPassword);
        });


    }


}
