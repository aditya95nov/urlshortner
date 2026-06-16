package org.urlshortner.repository;

import org.urlshortner.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository
        extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByOriginalUrl(String url);

    Optional<UrlMapping> findByShortCode(String code);

    boolean existsByShortCode(String code);
}