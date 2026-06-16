package org.urlshortner.dto;

public record ShortenResponse(
        String code,
        String shortUrl
) {}