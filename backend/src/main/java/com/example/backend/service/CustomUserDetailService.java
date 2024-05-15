package com.example.backend.service;

import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Endpoints.dto.UserLoginDto;
import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtTokenizer;
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
import java.util.List;

@Service
public class CustomUserDetailService implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        return null;
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
