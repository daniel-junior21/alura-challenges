package com.alura.challenge.aluraflix.controller;

import com.alura.challenge.aluraflix.dto.AuthResponseDTO;
import com.alura.challenge.aluraflix.dto.UserRequestDTO;
import com.alura.challenge.aluraflix.entities.User;
import com.alura.challenge.aluraflix.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<AuthResponseDTO> auth(@RequestBody @Valid UserRequestDTO userRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateAuthToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new AuthResponseDTO(tokenJWT, "3600"));
    }
}
