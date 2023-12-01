package com.alura.challenge.aluraflix.controller;

import com.alura.challenge.aluraflix.config.ConfigProperties;
import com.alura.challenge.aluraflix.dto.VideoRequestDTO;
import com.alura.challenge.aluraflix.dto.VideoResponseDTO;
import com.alura.challenge.aluraflix.dto.VideoUpdateRequestDTO;
import com.alura.challenge.aluraflix.dto.VideoListResponseDTO;
import com.alura.challenge.aluraflix.entities.Category;
import com.alura.challenge.aluraflix.entities.Video;
import com.alura.challenge.aluraflix.repositories.CategoryRepository;
import com.alura.challenge.aluraflix.repositories.VideoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class VideoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JacksonTester<VideoRequestDTO> videoRequestDtoJson;

    @Autowired
    JacksonTester<VideoResponseDTO> videoResponseDtoJson;

    @Autowired
    JacksonTester<VideoListResponseDTO> videosListResponseDtoJson;

    @Autowired
    JacksonTester<VideoUpdateRequestDTO> videoUpdateRequestDtoJson;

    @Autowired
    JacksonTester<Page<VideoResponseDTO>> videoResponsePageJson;

    @MockBean
    VideoRepository videoRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    ConfigProperties props;

    @Test
    @DisplayName("Should return error 400 trying to create a video without information")
    @WithMockUser
    void createVideoReturnError400() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/videos")).andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return error 404 trying to create a video, category not found")
    @WithMockUser
    void createVideoReturn404() throws Exception {
        String requestBody = videoRequestDtoJson.write(new VideoRequestDTO("Spring For Begginers", "Introduction to Spring.", "https://youtube.com/spring-for-begginers", 2L)).getJson();
        MockHttpServletResponse response = mockMvc
                .perform(post("/videos").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return success trying to create a video")
    @WithMockUser
    void createVideoReturn201() throws Exception {
        String requestBody = videoRequestDtoJson.write(new VideoRequestDTO("Spring For Begginers", "Introduction to Spring.", "https://youtube.com/spring-for-begginers", 1L)).getJson();

        Optional<Category> category = Optional.of(new Category(1L, "FREE", "GREEN"));
        when(categoryRepository.findById(any())).thenReturn(category);

        MockHttpServletResponse response = mockMvc
                .perform(post("/videos").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        String expected = videoResponseDtoJson.write(new VideoResponseDTO(null, "Spring For Begginers", "Introduction to Spring.", "https://youtube.com/spring-for-begginers", category.get().getId())).getJson();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return an empty video list with status 200")
    @WithMockUser
    void getVideosEmptyList() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Video> emptyPageList = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(videoRepository.findAll(pageable)).thenReturn(emptyPageList);
        MockHttpServletResponse response = mockMvc
                .perform(get("/videos").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String expected = "\"content\":[]";

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(expected));
    }

    @Test
    @DisplayName("Should return an video list with status 200")
    @WithMockUser
    void getVideosList() throws Exception {
        Category category = new Category(1L, "FREE", "GREEN");
        List<Video> videos = new ArrayList<>();
        videos.add(new Video(1L, "Spring fog begginers", "Spring for begginers", "https://youtube.com/spring-for-begginers", category));
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Video> videosPage = new PageImpl<>(videos, pageable, 1);
        when(videoRepository.findAll(any(PageRequest.class))).thenReturn(videosPage);

        MockHttpServletResponse response = mockMvc
                .perform(get("/videos").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(videos.get(0).getTitle()));
    }

    @Test
    @DisplayName("Should return error 404 when inform an invalid id")
    @WithMockUser
    void getVideoByidReturn404() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/videos/1"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return a video with success when inform a valid id")
    @WithMockUser
    void getVideoByid() throws Exception {
        Category category = new Category(2L, "TECH", "GRAY");
        Optional<Video> video = Optional.of(new Video(2L, "Spring fog begginers", "Spring for begginers", "https://youtube.com/spring-for-begginers", category));

        when(videoRepository.findById(any())).thenReturn(video);

        MockHttpServletResponse response = mockMvc
                .perform(get("/videos/2").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String expected = videoResponseDtoJson.write(new VideoResponseDTO(video.get())).getJson();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return an empty list when search by a word with no videos")
    @WithMockUser
    void searchVideosByNameEmptyList() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Video> emptyPageList = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(videoRepository.searchVideoByTitle(any(), any())).thenReturn(emptyPageList);

        MockHttpServletResponse response = mockMvc
                .perform(get("/videos/").contentType(MediaType.APPLICATION_JSON).param("search", "Python"))
                .andReturn().getResponse();

        String expected = "\"content\":[]";

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(expected));
    }

    @Test
    @DisplayName("Should return a video list when search by a word with videos")
    @WithMockUser
    void searchVideosByNameSuccess() throws Exception {
        Category category = new Category(1L, "FREE", "GREEN");
        List<Video> videos = new ArrayList<>();
        videos.add(new Video(1L, "Spring fog begginers", "Spring for begginers", "https://youtube.com/spring-for-begginers", category));

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Video> videosPage = new PageImpl<>(videos, pageable, 1);
        when(videoRepository.searchVideoByTitle(any(), any())).thenReturn(videosPage);

        MockHttpServletResponse response = mockMvc
                .perform(get("/videos/").contentType(MediaType.APPLICATION_JSON).param("search", "Spring"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(videos.get(0).getTitle()));
    }

    @Test
    @DisplayName("Should return error 404 when inform an invalid id")
    @WithMockUser
    void updateVideoError404() throws Exception {
        String newTitle = "Spring for begginers";
        String requestBody = videoUpdateRequestDtoJson.write(new VideoUpdateRequestDTO(1000L, newTitle, null, null, null)).getJson();

        MockHttpServletResponse response = mockMvc
                .perform(put("/videos").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return not update information when inform a valid id but new field blank")
    @WithMockUser
    void updateVideoNotUpdate() throws Exception {
        Category category = new Category(1L, "FREE", "GREEN");
        Optional<Video> video = Optional.of(new Video(1L, "Spring fog begginers", "Spring for begginers", "https://youtube.com/spring-for-begginers", category));

        when(videoRepository.findById(any())).thenReturn(video);

        String newTitle = "  ";
        String requestBody = videoUpdateRequestDtoJson.write(new VideoUpdateRequestDTO(1L, newTitle, null, null, null)).getJson();

        MockHttpServletResponse response = mockMvc
                .perform(put("/videos").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        Video videoUpdated = video.get();
        String expected = videoResponseDtoJson.write(new VideoResponseDTO(videoUpdated)).getJson();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return an updated information when inform a valid id")
    @WithMockUser
    void updateVideo() throws Exception {
        Category category = new Category(1L, "FREE", "GREEN");
        Optional<Video> video = Optional.of(new Video(1L, "Spring fog begginers", "Spring for begginers", "https://youtube.com/spring-for-begginers", category));

        when(videoRepository.findById(any())).thenReturn(video);

        String newTitle = "Spring for begginers";
        String requestBody = videoUpdateRequestDtoJson.write(new VideoUpdateRequestDTO(1L, newTitle, null, null, null)).getJson();

        MockHttpServletResponse response = mockMvc
                .perform(put("/videos").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        Video videoUpdated = video.get();
        videoUpdated.setTitle(newTitle);
        String expected = videoResponseDtoJson.write(new VideoResponseDTO(videoUpdated)).getJson();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return error 404 when inform an invalid id")
    @WithMockUser
    void deleteVideoError404() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(delete("/videos/1"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return success when inform a valid id")
    @WithMockUser
    void deleteVideoSuccess() throws Exception {
        Category category = new Category(1L, "FREE", "GREEN");
        Optional<Video> video = Optional.of(new Video(1L, "Spring fog begginers", "Spring for begginers", "https://youtube.com/spring-for-begginers", category));

        when(videoRepository.findById(any())).thenReturn(video);

        MockHttpServletResponse response = mockMvc
                .perform(delete("/videos/1"))
                .andReturn().getResponse();

        String expected = props.getMessageVideoDeleted();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return a list with 10 free videos")
    void freeVideosSuccess() throws Exception {
        List<Video> videos = getMockVideos();
        when(videoRepository.getFreeVideos()).thenReturn(videos);

        MockHttpServletResponse response = mockMvc
                .perform(get("/videos/free"))
                .andReturn().getResponse();

        String expected = "";

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(10, videos.size());
    }

    private List<Video> getMockVideos() {
        List<Video> videos = new ArrayList<>();

        for(Integer i = 0; i < 10; i++){
            Category category = new Category(1L, "FREE", "GREEN");
            Video video = new Video(i.longValue(), String.format("%d-Free", i), "Free", "https://www.youtube.com.br/free",category);
            videos.add(video);
        }

        return videos;
    }
}