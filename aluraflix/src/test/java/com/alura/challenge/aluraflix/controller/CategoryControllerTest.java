package com.alura.challenge.aluraflix.controller;

import com.alura.challenge.aluraflix.config.ConfigProperties;
import com.alura.challenge.aluraflix.dto.*;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvC;

    @Autowired
    JacksonTester<CategoryResponseDTO> categoryResponseDtoJson;

    @Autowired
    JacksonTester<CategoryRequestDTO> categoryRequestDtoJson;

    @Autowired
    JacksonTester<CategoryListResponseDTO> categoryListResponseDtoJson;

    @Autowired
    JacksonTester<VideoListResponseDTO> videosListResponseDtoJson;

    @Autowired
    JacksonTester<CategoryUpdateRequestDTO> categoryUpdateRequestDtoJson;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    VideoRepository videoRepository;

    @Autowired
    ConfigProperties props;

    @Test
    @DisplayName("Should return error 400 when inform invalid body")
    @WithMockUser
    void createCategoryError400() throws Exception {
        MockHttpServletResponse response = mockMvC
                .perform(post("/categories"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should create a category with success")
    @WithMockUser
    void createCategorySuccess() throws Exception {
        Category category = new Category(null, "Games", "Orange");
        String requestBody = categoryRequestDtoJson.write(new CategoryRequestDTO("Games", "Orange")).getJson();

        MockHttpServletResponse response = mockMvC
                .perform(post("/categories").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        String expected = categoryResponseDtoJson.write(new CategoryResponseDTO(category)).getJson();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return an empty category list")
    @WithMockUser
    void getCategoriesEmptyList() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Category> categoriesPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(categoryRepository.findAll(pageable)).thenReturn(categoriesPage);
        MockHttpServletResponse response = mockMvC
                .perform(get("/categories").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String expected = "\"content\":[]";

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(expected));
    }

    @Test
    @DisplayName("Should return a category list fullfilled")
    @WithMockUser
    void getCategoriesSuccess() throws Exception {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category(1L, "Free", "Green");
        Category category2 = new Category(2L, "Games", "Orange");
        categories.add(category1);
        categories.add(category2);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Category> categoriesPage = new PageImpl<>(categories, pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(categoriesPage);

        MockHttpServletResponse response = mockMvC
                .perform(get("/categories").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String expected = categoriesPage.getContent().get(0).getTitle();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(expected));
    }

    @Test
    @DisplayName("Should return error 404 when inform an invalid id")
    @WithMockUser
    void getCategoryByIdError404() throws Exception {
        MockHttpServletResponse response = mockMvC
                .perform(get("/categories/1"))
                .andReturn().getResponse();

        assertEquals( HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return success when inform a valid id")
    @WithMockUser
    void getCategoryByIdSuccess() throws Exception {
        Optional<Category> categoryOptional = Optional.of(new Category(1L, "Free", "Green"));
        when(categoryRepository.findById(any())).thenReturn(categoryOptional);

        MockHttpServletResponse response = mockMvC
                .perform(get("/categories/1").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String expected = categoryResponseDtoJson.write(new CategoryResponseDTO(categoryOptional.get())).getJson();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return error 404 when inform an invalid id")
    @WithMockUser
    void getVideosByCategoryError404() throws Exception {
        MockHttpServletResponse response = mockMvC
                .perform(get("/categories/1/videos"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return an empty video list when the category have no videos")
    @WithMockUser
    void getVideosByCategoryEmptyList() throws Exception {
        Optional<Category> categoryOptional = Optional.of(new Category(1L, "Free", "Green"));
        when(categoryRepository.findById(any())).thenReturn(categoryOptional);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Video> videosPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(videoRepository.findAllVideosByCategory(any(), any())).thenReturn(videosPage);

        MockHttpServletResponse response = mockMvC
                .perform(get("/categories/1/videos").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String expected = "\"content\":[]";

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(expected));
    }

    @Test
    @DisplayName("Should return a video list when the category have videos")
    @WithMockUser
    void getVideosByCategorySuccess() throws Exception {
        Optional<Category> categoryOptional = Optional.of(new Category(1L, "Free", "Green"));
        when(categoryRepository.findById(any())).thenReturn(categoryOptional);

        List<Video> videos = new ArrayList<>();
        Video video = new Video(1L, "Spring Advanced", "Spring advanced course", "https://youtube.com/spring-for-begginers", categoryOptional.get());
        videos.add(video);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Video> videosPage = new PageImpl<>(videos, pageable, 1);
        when(videoRepository.findAllVideosByCategory(any(), any())).thenReturn(videosPage);
        MockHttpServletResponse response = mockMvC
                .perform(get("/categories/1/videos").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(videos.get(0).getTitle()));
    }

    @Test
    @DisplayName("Should return error 404 when inform an invalid id")
    @WithMockUser
    void updateCategoryErro404() throws Exception {
        String requestBody = categoryUpdateRequestDtoJson.write(new CategoryUpdateRequestDTO(1L, "new title", null)).getJson();

        MockHttpServletResponse response = mockMvC
                .perform(put("/categories").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return not update information when inform a valid id but new field blank")
    @WithMockUser
    void updateCategoryNotUpdate() throws Exception {
        Optional<Category> categoryOptional = Optional.of(new Category(1L, "Free", "Green"));
        when(categoryRepository.findById(any())).thenReturn(categoryOptional);

        String newTitle = "  ";
        String requestBody = categoryUpdateRequestDtoJson.write(new CategoryUpdateRequestDTO(1L, newTitle, null)).getJson();

        MockHttpServletResponse response = mockMvC
                .perform(put("/categories").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        String expected = categoryResponseDtoJson.write(new CategoryResponseDTO(categoryOptional.get())).getJson();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return an updated information when inform a valid id")
    @WithMockUser
    void updateCategoryUpdatedSuccess() throws Exception {
        Optional<Category> categoryOptional = Optional.of(new Category(2L, "Gems", "Orange"));
        when(categoryRepository.findById(any())).thenReturn(categoryOptional);

        String newTitle = "Games";
        String requestBody = categoryUpdateRequestDtoJson.write(new CategoryUpdateRequestDTO(2L, newTitle, null)).getJson();

        MockHttpServletResponse response = mockMvC
                .perform(put("/categories").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn().getResponse();

        Category category = categoryOptional.get();
        category.setTitle(newTitle);
        String expected = categoryResponseDtoJson.write(new CategoryResponseDTO(category)).getJson();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    @DisplayName("Should return error 404 when inform an invalid id")
    @WithMockUser
    void deleteCategoryError404() throws Exception {
        MockHttpServletResponse response = mockMvC
                .perform(delete("/categories/1"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should delete a category with success")
    @WithMockUser
    void deleteCategorySuccess() throws Exception {
        Optional<Category> categoryOptional = Optional.of(new Category(2L, "Gems", "Orange"));
        when(categoryRepository.findById(any())).thenReturn(categoryOptional);

        MockHttpServletResponse response = mockMvC
                .perform(delete("/categories/2"))
                .andReturn().getResponse();

        String expected = props.getMessageCategoryDeleted();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }
}