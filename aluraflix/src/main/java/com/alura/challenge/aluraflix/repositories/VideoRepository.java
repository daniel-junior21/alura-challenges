package com.alura.challenge.aluraflix.repositories;

import com.alura.challenge.aluraflix.entities.Category;
import com.alura.challenge.aluraflix.entities.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("select v from Video v where v.category = :category")
    Page<Video> findAllVideosByCategory(Category category, Pageable pageable);

    @Query("select v from Video v where v.title like concat('%', :title, '%')")
    Page<Video> searchVideoByTitle(String title, Pageable pageable);

    @Query(value = "select v from Video v order by rand() limit 10")
    List<Video> getFreeVideos();
}
