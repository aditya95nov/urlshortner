package org.urlshortner.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<?> notFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AliasAlreadyExistsException.class)
    public ResponseEntity<?> aliasExists() {
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<?> invalidUrl() {
        return ResponseEntity.badRequest().build();
    }
}