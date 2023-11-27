package com.alura.challenge.aluraflix.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
        @NotBlank(message = "Title must not be empty")
        String title,
        @NotBlank(message = "Color must not be empty")
        String color) {
}
