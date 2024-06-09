package com.example.backend.Endpoints.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import lombok.Builder;

@RecordBuilder
public record UserLoginDto(
        String email,
        String password
) {

}
