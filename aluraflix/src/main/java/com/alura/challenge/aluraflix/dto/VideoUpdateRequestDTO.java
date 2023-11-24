package com.alura.challenge.aluraflix.dto;

import jakarta.validation.constraints.NotNull;

public record VideoUpdateRequestDTO(
        @NotNull
        Long id,
        String title,
        String description,
        String url) {
}
