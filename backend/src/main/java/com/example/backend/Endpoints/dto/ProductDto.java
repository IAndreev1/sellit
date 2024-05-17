package com.example.backend.Endpoints.dto;

public record ProductDto(
        Long id,

        String name,

        String description,

        Double price,

        byte[] imageData
) {
}
