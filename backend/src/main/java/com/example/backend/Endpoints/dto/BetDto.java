package com.example.backend.Endpoints.dto;

import com.example.backend.Entity.ApplicationUser;
import com.example.backend.Entity.Product;

public record BetDto(
       Long id,
       String description,
       Double amount,
       UserDetailDto user,
       ProductDto product


) {

}
