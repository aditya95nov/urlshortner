package org.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException() {
        super("Short URL not found");
    }

    public UrlNotFoundException(String code) {
        super("Short URL not found for code: " + code);
    }
}