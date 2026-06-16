package org.urlshortner.service;

import org.urlshortner.exception.InvalidUrlException;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@Component
public class UrlValidator {

    public void validate(String url) {

        try {
            URI uri = new URI(url);

            if (uri.getScheme() == null ||
                    uri.getHost() == null) {
                throw new InvalidUrlException();
            }

            if (!Set.of("http", "https")
                    .contains(uri.getScheme().toLowerCase())) {
                throw new InvalidUrlException();
            }

        } catch (URISyntaxException e) {
            throw new InvalidUrlException();
        }
    }
}