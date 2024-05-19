package com.example.backend.Endpoints.dto;

import lombok.Builder;

@Builder
public record ProductDto(
        Long id,
        String name,
        String description,
        Double price,
        byte[] imageData
) {
}
