package com.alura.challenge.aluraflix.util;

import com.alura.challenge.aluraflix.util.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
