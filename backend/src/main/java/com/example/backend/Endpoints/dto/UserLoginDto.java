package com.example.backend.Endpoints.dto;

public record UserLoginDto(
        String email,
        String password
) {
}
