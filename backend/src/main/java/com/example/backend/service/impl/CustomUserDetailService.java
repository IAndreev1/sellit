package com.example.backend.service.impl;

import com.example.backend.Endpoints.dto.ChangePasswordDto;
import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Endpoints.dto.UserLoginDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Exceptions.AuthorizationException;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.NotFoundException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtTokenizer;
import com.example.backend.service.UserService;
import com.example.backend.service.impl.validators.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserValidator userValidator;
    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.userValidator = userValidator;
    }


    @Override
    public ApplicationUser getUser(String authToken) {
        LOGGER.trace("getUser({})", authToken);
        String email = jwtTokenizer.getEmailFromToken(authToken);
        return userRepository.findUserByEmail(email);
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        LOGGER.trace("findApplicationUserByEmail({})", email);
        ApplicationUser applicationUser = userRepository.findUserByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public String login(UserLoginDto userLoginDto) throws ValidationException, ConflictException {
        LOGGER.trace("login({})", userLoginDto);

        UserDetails userDetails = loadUserByUsername(userLoginDto.email());
        if (userDetails != null
                && userDetails.isAccountNonExpired()
                && userDetails.isAccountNonLocked()
                && userDetails.isCredentialsNonExpired()
                && passwordEncoder.matches(userLoginDto.password(), userDetails.getPassword())
        ) {
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
        }
        throw new ConflictException("Username or password is incorrect or account is locked");
    }

    @Override
    public String register(UserDetailDto userDetailDto) throws ValidationException, ConflictException {
        userValidator.validateForCreate(userDetailDto);
        LOGGER.trace("register({})", userDetailDto);
        ApplicationUser newUser = new ApplicationUser();
        newUser.setFirstName(userDetailDto.firstName());
        newUser.setLastName(userDetailDto.lastName());
        newUser.setEmail(userDetailDto.email());
        newUser.setPassword(passwordEncoder.encode(userDetailDto.password()));
        userRepository.save(newUser);

        UserDetails userDetails = loadUserByUsername(userDetailDto.email());
        if (userDetails != null) {
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
        }

        throw new ConflictException("Failed to register the user");
    }

    @Override
    public boolean changeUserName(String newUserName) {
        return false;
    }

    @Override
    public boolean changePassword(ChangePasswordDto changePasswordDto) throws AuthorizationException {
        ApplicationUser user = findApplicationUserByEmail(changePasswordDto.email());
        if (passwordEncoder.matches(changePasswordDto.oldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
            userRepository.save(user);
        } else {
            throw new AuthorizationException("Old Password does not match", new LinkedList<>());
        }
        return false;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        ApplicationUser applicationUser = userRepository.findUserByEmail(email);
        if (applicationUser.getAdmin() == null) {
            applicationUser.setAdmin(false);
        }
        List<GrantedAuthority> grantedAuthorities;
        if (applicationUser.getAdmin()) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
        } else {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        }

        User toRet = new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);


        return toRet;

    }


}
