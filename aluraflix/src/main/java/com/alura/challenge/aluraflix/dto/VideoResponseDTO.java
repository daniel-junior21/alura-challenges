package com.alura.challenge.aluraflix.dto;

import com.alura.challenge.aluraflix.entities.Video;

public record VideoResponseDTO(Long id, String title, String description, String url, Long category) {
    public VideoResponseDTO(Video video) {
        this(video.getId(), video.getTitle(), video.getDescription(), video.getUrl(), video.getCategory().getId());
    }
}
