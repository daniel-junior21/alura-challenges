package com.alura.challenge.aluraflix.entities;

import com.alura.challenge.aluraflix.controller.VideoUpdateRequestDTO;
import com.alura.challenge.aluraflix.dto.VideoRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String url;

    public Video(VideoRequestDTO videoRequest) {
        this.title = videoRequest.title();
        this.description = videoRequest.description();
        this.url = videoRequest.url();
    }

    public void updateVideo(VideoUpdateRequestDTO videoUpdateRequest) {
        if(videoUpdateRequest.title() != null) {
            this.title = videoUpdateRequest.title();
        }

        if(videoUpdateRequest.description() != null) {
            this.description = videoUpdateRequest.description();
        }

        if(videoUpdateRequest.url() != null) {
            this.url = videoUpdateRequest.url();
        }
    }
}