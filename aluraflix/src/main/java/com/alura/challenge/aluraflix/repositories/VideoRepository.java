package com.alura.challenge.aluraflix.repositories;

import com.alura.challenge.aluraflix.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
