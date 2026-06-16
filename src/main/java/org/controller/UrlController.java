package org.controller;

import lombok.RequiredArgsConstructor;
import org.dto.ShortenRequest;
import org.dto.ShortenResponse;
import org.service.UrlShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(
            @RequestBody ShortenRequest request) {

        return ResponseEntity.ok(
                service.shorten(
                        request.url(),
                        request.alias()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(
            @PathVariable String code) {

        String original =
                service.resolve(code);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(original))
                .build();
    }
}
