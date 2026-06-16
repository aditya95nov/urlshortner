package org.urlshortner.exception;

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException() {
        super("Invalid URL");
    }

    public InvalidUrlException(String url) {
        super("Invalid URL: " + url);
    }
}