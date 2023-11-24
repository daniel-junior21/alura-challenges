package com.alura.challenge.aluraflix.controller;

import com.alura.challenge.aluraflix.dto.CategoryRequestDTO;
import com.alura.challenge.aluraflix.dto.CategoryResponseDTO;
import com.alura.challenge.aluraflix.dto.CategoryUpdateRequestDTO;
import com.alura.challenge.aluraflix.entities.Category;
import com.alura.challenge.aluraflix.repositories.CategoryRepository;
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

    @PostMapping
    @Transactional
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryRequestDTO categoryRequest, UriComponentsBuilder uriComponentsBuilder) {
        Category category = new Category(categoryRequest);
        categoryRepository.save(category);

        URI uri = uriComponentsBuilder.path("/categories/{id}").buildAndExpand(category.getId()).toUri();

        return ResponseEntity.created(uri).body(new CategoryResponseDTO(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        List<CategoryResponseDTO> categories = categoryRepository.findAll().stream()
                .map(CategoryResponseDTO::new).toList();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id") Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty()) {
            throw new NotFoundException("Category Not Found");
        }

        Category category = categoryOptional.get();

        return ResponseEntity.ok(new CategoryResponseDTO(category));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<CategoryResponseDTO> updateCategory(@RequestBody @Valid CategoryUpdateRequestDTO categoryUpdateRequest) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryUpdateRequest.id());
        if(categoryOptional.isEmpty()) {
            throw new NotFoundException("Category Not Found");
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
            throw new NotFoundException("Category Not Found");
        }

        Category category = categoryOptional.get();
        try {
            categoryRepository.delete(category);
        } catch(Exception e) {
            throw new InternalError("An error occurred trying to delete the category!");
        }

        return ResponseEntity.ok("Category Deleted Successfully!");
    }
}
