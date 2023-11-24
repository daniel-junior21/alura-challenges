package com.alura.challenge.aluraflix.controller;

import jakarta.validation.constraints.NotNull;

public record VideoUpdateRequestDTO(
        @NotNull
        Long id,
        String title,
        String description,
        String url) {
}
