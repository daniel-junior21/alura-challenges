package com.alura.challenge.aluraflix.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank
        String username,
        @NotBlank
        String password) {
}
