package com.example.backend.Endpoints.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import lombok.Builder;

@RecordBuilder
public record ProductSearchDto(
        Long id,
        String name,
        String description,
        Double priceFrom,
        Double priceTo
        ) {
}