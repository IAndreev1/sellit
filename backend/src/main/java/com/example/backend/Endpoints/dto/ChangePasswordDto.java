package com.example.backend.Endpoints.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record ChangePasswordDto(
        String email,
        String oldPassword,
        String newPassword
) {
}
