package org.urlshortner.dto;

public record ShortenRequest(
        String url,
        String alias
) {}
