package com.alura.challenge.aluraflix.dto;

import jakarta.validation.constraints.NotBlank;

public record VideoRequestDTO(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank
        String url) {
}
