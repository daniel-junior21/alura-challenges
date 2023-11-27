package com.alura.challenge.aluraflix.controller;

import com.alura.challenge.aluraflix.config.ConfigProperties;
import com.alura.challenge.aluraflix.dto.*;
import com.alura.challenge.aluraflix.entities.Category;
import com.alura.challenge.aluraflix.repositories.CategoryRepository;
import com.alura.challenge.aluraflix.repositories.VideoRepository;
import com.alura.challenge.aluraflix.util.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    ConfigProperties props;

    @PostMapping
    @Transactional
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryRequestDTO categoryRequest, UriComponentsBuilder uriComponentsBuilder) {
        Category category = new Category(categoryRequest);
        categoryRepository.save(category);

        URI uri = uriComponentsBuilder.path("/categories/{id}").buildAndExpand(category.getId()).toUri();

        return ResponseEntity.created(uri).body(new CategoryResponseDTO(category));
    }

    @GetMapping
    public ResponseEntity<CategoryListResponseDTO> getCategories() {
        List<CategoryResponseDTO> categories = categoryRepository.findAll().stream()
                .map(CategoryResponseDTO::new).toList();

        return ResponseEntity.ok(new CategoryListResponseDTO(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id") Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty()) {
            throw new NotFoundException(props.getMessageCategoryNotFound());
        }

        Category category = categoryOptional.get();

        return ResponseEntity.ok(new CategoryResponseDTO(category));
    }

    @GetMapping("/{id}/videos")
    public ResponseEntity<VideoListResponseDTO> getVideosByCategory(@PathVariable("id") Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty()) {
            throw new NotFoundException(props.getMessageCategoryNotFound());
        }

        List<VideoResponseDTO> videos = videoRepository.findAllVideosByCategory(categoryOptional.get()).stream()
                .map(VideoResponseDTO::new).toList();

        return ResponseEntity.ok(new VideoListResponseDTO(videos));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<CategoryResponseDTO> updateCategory(@RequestBody @Valid CategoryUpdateRequestDTO categoryUpdateRequest) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryUpdateRequest.id());
        if(categoryOptional.isEmpty()) {
            throw new NotFoundException(props.getMessageCategoryNotFound());
        }

        Category category = categoryOptional.get();
        category.update(categoryUpdateRequest);

        return ResponseEntity.ok(new CategoryResponseDTO(category));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty()) {
            throw new NotFoundException(props.getMessageCategoryNotFound());
        }

        Category category = categoryOptional.get();

        categoryRepository.delete(category);

        return ResponseEntity.ok(props.getMessageCategoryDeleted());
    }
}
