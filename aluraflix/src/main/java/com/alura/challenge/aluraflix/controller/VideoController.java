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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<VideoResponseDTO>> getVideos(@PageableDefault(size = 10) Pageable pagination) {
        Page<VideoResponseDTO> videos = videoRepository.findAll(pagination).map(VideoResponseDTO::new);

        return ResponseEntity.ok(videos);
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
    public ResponseEntity<Page<VideoResponseDTO>> searchVideosByName(@RequestParam(name = "search") String title, @PageableDefault(size = 10) Pageable pageable) {
        Page<VideoResponseDTO> videos = videoRepository.searchVideoByTitle(title, pageable).map(VideoResponseDTO::new);

        return ResponseEntity.ok(videos);
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
