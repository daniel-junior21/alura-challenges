package com.alura.challenge.aluraflix.dto;

import jakarta.validation.constraints.NotBlank;

public record VideoRequestDTO(
        @NotBlank(message = "Title field must not be empty")
        String title,
        @NotBlank(message = "Description field must not be empty")
        String description,
        @NotBlank(message = "Url field must not be empty")
        String url,
        Long category) {
}
