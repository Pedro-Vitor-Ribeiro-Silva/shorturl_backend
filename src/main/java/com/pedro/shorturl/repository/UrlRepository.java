package com.pedro.shorturl.repository;

import com.pedro.shorturl.domain.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UrlRepository extends MongoRepository<Url, String> {

    Optional<Url> findByShortCode(String shortCode);

    Optional<Url> findByOriginalUrl(String originalUrl);
}