package com.alura.challenge.aluraflix.dto;

import com.alura.challenge.aluraflix.entities.Category;

public record CategoryResponseDTO(Long id, String title, String color) {
    public CategoryResponseDTO(Category category) {
        this(category.getId(), category.getTitle(), category.getColor());
    }
}
