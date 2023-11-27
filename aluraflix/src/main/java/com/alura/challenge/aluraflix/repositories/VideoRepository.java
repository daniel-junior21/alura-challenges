package com.alura.challenge.aluraflix.repositories;

import com.alura.challenge.aluraflix.entities.Category;
import com.alura.challenge.aluraflix.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("select v from Video v where v.category = :category")
    List<Video> findAllVideosByCategory(Category category);

    @Query("select v from Video v where v.title like concat('%', :title, '%')")
    List<Video> searchVideoByTitle(String title);
}
