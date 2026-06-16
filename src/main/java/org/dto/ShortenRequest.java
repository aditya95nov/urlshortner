package org.dto;

public record ShortenRequest(
        String url,
        String alias
) {}
