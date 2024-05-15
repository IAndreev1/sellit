package com.example.backend.Endpoints.dto;

import lombok.Builder;

@Builder
public record UserLoginDto(
        String email,
        String password
) {

}
