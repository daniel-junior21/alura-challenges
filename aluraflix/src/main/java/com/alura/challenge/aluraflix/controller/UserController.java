package com.alura.challenge.aluraflix.controller;

import com.alura.challenge.aluraflix.dto.UserRequestDTO;
import com.alura.challenge.aluraflix.dto.UserResponseDTO;
import com.alura.challenge.aluraflix.entities.User;
import com.alura.challenge.aluraflix.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encryptedPass = encoder.encode(userRequest.password());

        User user = new User(userRequest.username(), encryptedPass);
        userRepository.save(user);

        return ResponseEntity.ok().body(new UserResponseDTO(user.getId(), user.getUsername()));
    }
}
