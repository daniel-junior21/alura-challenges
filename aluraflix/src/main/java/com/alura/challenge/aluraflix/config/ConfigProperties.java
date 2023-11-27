package com.alura.challenge.aluraflix.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@NoArgsConstructor
public class ConfigProperties {
    private final String messageVideoNotFound = "Video Not Found";
    private final String messageVideoError = "An error occurred trying to delete the video!";
    private final String messageVideoDeleted = "Video Deleted Successfully!";
    private final String messageCategoryNotFound = "Category Not Found";
    private final String messageCategoryError = "An error occurred trying to delete the category!";
    private final String messageCategoryDeleted = "Category Deleted Successfully!";
}
