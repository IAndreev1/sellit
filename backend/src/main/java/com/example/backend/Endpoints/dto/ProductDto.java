package com.example.backend.Endpoints.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@RecordBuilder
public record ProductDto(
        Long id,
        @NotBlank(message = "Product name is required")
        @Size(max = 100, message = "Product name cannot exceed 100 characters")
        String name,
        @Size(max = 100, message = "Product description cannot exceed 500 characters")
        String description,
        @NotNull(message = "Product price is required")
        Double price,
        boolean sold,
        UserDetailDto user,
        byte[] imageData
) {
}
