package com.alura.challenge.aluraflix.entities;

import com.alura.challenge.aluraflix.dto.CategoryRequestDTO;
import com.alura.challenge.aluraflix.dto.CategoryUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String color;

    public Category(CategoryRequestDTO categoryRequest) {
        this.title = categoryRequest.title();
        this.color = categoryRequest.color();
    }

    public void update(CategoryUpdateRequestDTO categoryUpdateRequest) {
        if(categoryUpdateRequest.title() != null && !categoryUpdateRequest.title().trim().isEmpty()) {
            this.title = categoryUpdateRequest.title();
        }

        if(categoryUpdateRequest.color() != null && !categoryUpdateRequest.color().trim().isEmpty()) {
            this.color = categoryUpdateRequest.color();
        }
    }
}
