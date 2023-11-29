package com.alura.challenge.aluraflix.util;

import com.alura.challenge.aluraflix.util.exceptions.NotFoundException;
import com.alura.challenge.aluraflix.util.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> notFoundException(NotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InternalError.class)
    public ResponseEntity<StandardError> exception(InternalError exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError error = new StandardError(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<StandardError>> invalidRequestException(MethodArgumentNotValidException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<StandardError> errors = exception.getAllErrors().stream()
                .map(fieldError -> new StandardError(fieldError.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(status).body(errors);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> unauthorizedException(UnauthorizedException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError(exception.getMessage());

        return ResponseEntity.status(status).body(error);
    }
}
