package unit;

import org.urlshortner.entity.UrlMapping;
import org.urlshortner.exception.AliasAlreadyExistsException;
import org.urlshortner.exception.UrlNotFoundException;
import org.urlshortner.generator.Base62Encoder;
import org.urlshortner.repository.UrlMappingRepository;
import org.urlshortner.service.UrlShortenerService;
import org.urlshortner.service.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UrlShortenerServiceTest {

    private UrlMappingRepository repository;
    private UrlValidator validator;
    private Base62Encoder encoder;
    private UrlShortenerService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(UrlMappingRepository.class);
        validator = Mockito.mock(UrlValidator.class);
        encoder = new Base62Encoder();

        service = new UrlShortenerService(
                repository,
                encoder,
                validator
        );
    }

    @Test
    void shouldReturnExistingCodeForDuplicateUrl() {

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl("https://google.org");
        mapping.setShortCode("abc");

        when(repository.findByOriginalUrl("https://google.org"))
                .thenReturn(Optional.of(mapping));

        var response =
                service.shorten("https://google.org", null);

        assertEquals("abc", response.code());
    }

    @Test
    void shouldThrowWhenAliasAlreadyExists() {

        when(repository.findByOriginalUrl(any()))
                .thenReturn(Optional.empty());

        when(repository.existsByShortCode("google"))
                .thenReturn(true);

        assertThrows(
                AliasAlreadyExistsException.class,
                () -> service.shorten(
                        "https://google.org",
                        "google"
                )
        );
    }

    @Test
    void shouldThrowWhenCodeNotFound() {

        when(repository.findByShortCode("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UrlNotFoundException.class,
                () -> service.resolve("unknown")
        );
    }
}