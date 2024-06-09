package com.example.backend.Endpoints.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@RecordBuilder
public record ProductSearchDto(
        Long id,
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        @Min(value = 0, message = "PriceFrom must be greater than or equal to 0")
        Double priceFrom,
        @Min(value = 0, message = "PriceTo must be greater than or equal to 0")
        Double priceTo
) {
}