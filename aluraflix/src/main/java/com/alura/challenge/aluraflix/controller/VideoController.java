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
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ConfigProperties props;

    @PostMapping
    @Transactional
    public ResponseEntity<VideoResponseDTO> createVideo(@RequestBody @Valid VideoRequestDTO videoRequest, UriComponentsBuilder uriComponentsBuilder) {
        Long idCategory = 1L;
        if(videoRequest.category() != null) {
            idCategory = videoRequest.category();
        }

        Optional<Category> category = categoryRepository.findById(idCategory);
        if(category.isEmpty()) {
            throw new NotFoundException(props.getMessageCategoryNotFound());
        }

        Video video = new Video(videoRequest, category.get());
        videoRepository.save(video);

        URI uri = uriComponentsBuilder.path("/videos/{id}").buildAndExpand(video.getId()).toUri();

        return ResponseEntity.created(uri).body(new VideoResponseDTO(video));
    }

    @GetMapping
    public ResponseEntity<VideoListResponseDTO> getVideos() {
        List<VideoResponseDTO> videos = videoRepository.findAll().stream().map(VideoResponseDTO::new).toList();

        return ResponseEntity.ok(new VideoListResponseDTO(videos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponseDTO> getVideoByid(@PathVariable("id") Long id) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if(videoOptional.isEmpty()) {
            throw new NotFoundException(props.getMessageVideoNotFound());
        }

        return ResponseEntity.ok(new VideoResponseDTO(videoOptional.get()));
    }

    @GetMapping("/")
    public ResponseEntity<VideoListResponseDTO> searchVideosByName(@RequestParam(name = "search") String title) {
        List<VideoResponseDTO> videos = videoRepository.searchVideoByTitle(title).stream()
                .map(VideoResponseDTO::new).toList();

        return ResponseEntity.ok(new VideoListResponseDTO(videos));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<VideoResponseDTO> updateVideo(@RequestBody @Valid VideoUpdateRequestDTO videoUpdateRequest) {
        Optional<Video> videoOptional = videoRepository.findById(videoUpdateRequest.id());
        if(videoOptional.isEmpty()) {
            throw new NotFoundException(props.getMessageVideoNotFound());
        }

        Video video = videoOptional.get();
        video.updateVideo(videoUpdateRequest);

        return ResponseEntity.ok(new VideoResponseDTO(video));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteVideo(@PathVariable("id") Long id) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if(videoOptional.isEmpty()) {
            throw new NotFoundException(props.getMessageVideoNotFound());
        }

        videoRepository.delete(videoOptional.get());

        return ResponseEntity.ok(props.getMessageVideoDeleted());
    }
}
