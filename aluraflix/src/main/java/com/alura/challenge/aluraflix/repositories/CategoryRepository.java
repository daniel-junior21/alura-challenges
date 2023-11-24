package com.alura.challenge.aluraflix.repositories;

import com.alura.challenge.aluraflix.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
