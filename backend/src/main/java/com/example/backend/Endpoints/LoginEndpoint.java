package com.example.backend.Endpoints;

import com.example.backend.Endpoints.Mappers.UserMapper;
import com.example.backend.Endpoints.dto.UserLoginDto;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
@RestController
@RequestMapping(value = "/api/v1/authentication")
public class LoginEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public LoginEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    @PostMapping
    public String login(@RequestBody UserLoginDto userLoginDto) throws ValidationException, ConflictException {
        LOGGER.trace("login({})", userLoginDto);
        return userService.login(userLoginDto);
    }

}
