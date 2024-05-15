package com.example.backend.Endpoints.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
public record UserDetailDto(
        Long id,

        String firstName,

        String lastName,

        String email,

        String password

) {

}
