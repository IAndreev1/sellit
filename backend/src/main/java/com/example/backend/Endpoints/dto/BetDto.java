package com.example.backend.Endpoints.dto;

import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;
import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@RecordBuilder
public record BetDto(
        Long id,
        @Size(max = 100, message = "Description cannot exceed 100 characters")
        String description,
        @NotNull(message = "Amount is required")
        @Min(0)
        Double amount,
        LocalDate date,
        boolean accepted,
        boolean rejected,

        UserDetailDto user,
        @NotNull(message = "Product is required")
        ProductDto product


) {
        @AssertTrue(message = "Accepted and rejected cannot be true at the same time")
        boolean isNotBothAcceptedAndRejected() {
                return !(accepted && rejected);
        }

}
