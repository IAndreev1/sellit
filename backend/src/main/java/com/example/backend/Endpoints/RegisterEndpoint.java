package com.example.backend.Endpoints;

import com.example.backend.Endpoints.dto.UserDetailDto;
import com.example.backend.Exceptions.ConflictException;
import com.example.backend.Exceptions.ValidationException;
import com.example.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/register")
public class RegisterEndpoint {

    private final UserService userService;


    public RegisterEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PermitAll
    @PostMapping
    public String register(@RequestBody UserDetailDto userDetailDto) throws ValidationException, ConflictException {
        return userService.register(userDetailDto);
    }
}
