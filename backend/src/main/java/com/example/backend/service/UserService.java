package com.example.backend.service;

import com.example.backend.Endpoints.dto.ChangePasswordDto;
import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Endpoints.dto.UserLoginDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.ValidationException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service interface for managing user operations.
 */
public interface UserService extends UserDetailsService {
    ApplicationUser getUser(String authToken);

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * Authenticates a user based on the provided login details.
     *
     * @param userLoginDto the data transfer object containing user login details
     * @return a JWT token or session identifier if login is successful
     * @throws ValidationException if the login details are invalid
     * @throws ConflictException   if there is a conflict during the login process
     */
    String login(UserLoginDto userLoginDto) throws ValidationException, ConflictException;

    /**
     * Registers a new user based on the provided user details.
     *
     * @param userDetailDto the data transfer object containing user details for registration
     * @return a confirmation message or identifier if registration is successful
     * @throws ValidationException if the registration details are invalid
     * @throws ConflictException   if there is a conflict during the registration process, such as a duplicate email
     */
    String register(UserDetailDto userDetailDto) throws ValidationException, ConflictException;

    /**
     * Changes the username of the currently authenticated user.
     *
     * @param newUserName the new username
     * @return true if the username was successfully changed, false otherwise
     */
    boolean changeUserName(String newUserName);


    /**
     * Changes the password of the currently authenticated user.
     *
     * @param changePasswordDto the data transfer object containing the current and new passwords
     * @return true if the password was successfully changed, false otherwise
     * @throws AuthorizationException if the current password is incorrect or the user is not authorized to change the password
     */
    boolean changePassword(ChangePasswordDto changePasswordDto) throws AuthorizationException;


}
