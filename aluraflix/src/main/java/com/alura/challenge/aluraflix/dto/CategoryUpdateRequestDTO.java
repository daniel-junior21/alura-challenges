package com.alura.challenge.aluraflix.dto;

import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequestDTO(
        @NotNull
        Long id,
        String title,
        String color) {
}
