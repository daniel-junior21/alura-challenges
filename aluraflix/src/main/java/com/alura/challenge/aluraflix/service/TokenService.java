package com.alura.challenge.aluraflix.service;

import com.alura.challenge.aluraflix.config.ConfigProperties;
import com.alura.challenge.aluraflix.entities.User;
import com.alura.challenge.aluraflix.util.exceptions.UnauthorizedException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Autowired
    ConfigProperties props;

    public String generateAuthToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(props.getSecret());
            return JWT.create()
                    .withIssuer("Aluraflix")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId())
                    .withExpiresAt(getExpireDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating Auth Token: " + exception.getMessage());
        }
    }

    public String validateAuthToken(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(props.getSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Aluraflix")
                    .build();
            decodedJWT = verifier.verify(token);

            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception){
            throw new UnauthorizedException(props.getMessageUnauthorized());
        }
    }

    private Instant getExpireDate() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

}
