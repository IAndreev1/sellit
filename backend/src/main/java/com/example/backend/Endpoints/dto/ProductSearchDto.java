package com.example.backend.Endpoints.dto;

import lombok.Builder;

@Builder
public record ProductSearchDto(
        Long id,
        String name,
        String description,
        Double priceFrom,
        Double priceTo
        ) {
}