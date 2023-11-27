package com.alura.challenge.aluraflix.repositories;

import com.alura.challenge.aluraflix.entities.Category;
import com.alura.challenge.aluraflix.entities.Video;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class VideoRepositoryTest {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Should return all videos from a category with success")
    void findAllVideosByCategory() {
        Category category = createCategory("FREE", "GREEN");
        Video expected = createVideo("Spring for Begginers", "Spring for Begginers", "https://youtube.com/spring-for-begginers", category);
        List<Video> videos = videoRepository.findAllVideosByCategory(category);
        Video actual = videos.get(0);

        assertNotNull(videos);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should return an empty videos list from a category")
    void findAllVideosByCategoryReturnEmptyList() {
        Category category = createCategory("FREE", "GREEN");
        Category categorySearch = createCategory("Tech", "gray");
        Video video = createVideo("Spring for Begginers", "Spring for Begginers", "https://youtube.com/spring-for-begginers", category);
        List<Video> videos = videoRepository.findAllVideosByCategory(categorySearch);

        assertEquals(new ArrayList<>(), videos);
    }

    @Test
    @DisplayName("Should return a video from a title search")
    void searchVideoByTitle() {
        Category category = createCategory("FREE", "GREEN");
        Video expected = createVideo("Spring for Begginers", "Spring for Begginers", "https://youtube.com/spring-for-begginers", category);

        List<Video> result = videoRepository.searchVideoByTitle("Spring");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return an empty video list from a title search")
    void searchVideoByTitleReturnEmptyList() {
        Category category = createCategory("FREE", "GREEN");
        Video expected = createVideo("Spring for Begginers", "Spring for Begginers", "https://youtube.com/spring-for-begginers", category);

        List<Video> result = videoRepository.searchVideoByTitle("Python");

        assertEquals(new ArrayList<>(), result);
    }

    private Category createCategory(String title, String color) {
        Category category = new Category(null, title, color);
        em.persist(category);
        return category;
    }

    private Video createVideo(String title, String description, String url, Category category) {
        Video video = new Video(null, title, description, url, category);
        em.persist(video);
        return video;
    }
}