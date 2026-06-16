package org.urlshortner.service;

import lombok.RequiredArgsConstructor;
import org.urlshortner.dto.ShortenResponse;
import org.urlshortner.entity.UrlMapping;
import org.urlshortner.exception.AliasAlreadyExistsException;
import org.urlshortner.exception.UrlNotFoundException;
import org.urlshortner.generator.Base62Encoder;
import org.urlshortner.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UrlShortenerService {

    private final UrlMappingRepository repository;
    private final Base62Encoder encoder;
    private final UrlValidator validator;

    public ShortenResponse shorten(
            String url,
            String alias) {

        validator.validate(url);

        Optional<UrlMapping> existing =
                repository.findByOriginalUrl(url);

        if (existing.isPresent()) {

            return new ShortenResponse(
                    existing.get().getShortCode(),
                    "/"+existing.get().getShortCode());
        }

        if (alias != null && !alias.isBlank()) {

            if (repository.existsByShortCode(alias)) {
                throw new AliasAlreadyExistsException();
            }

            UrlMapping mapping = new UrlMapping();
            mapping.setOriginalUrl(url);
            mapping.setShortCode(alias);
            mapping.setCreatedAt(LocalDateTime.now());

            repository.save(mapping);

            return new ShortenResponse(
                    alias,
                    "/"+alias);
        }

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(url);
        mapping.setCreatedAt(LocalDateTime.now());

        repository.save(mapping);

        String code = encoder.encode(mapping.getId());

        mapping.setShortCode(code);

        repository.save(mapping);

        return new ShortenResponse(
                code,
                "/"+code);
    }

    public String resolve(String code) {

        return repository.findByShortCode(code)
                .map(UrlMapping::getOriginalUrl)
                .orElseThrow(UrlNotFoundException::new);
    }
}