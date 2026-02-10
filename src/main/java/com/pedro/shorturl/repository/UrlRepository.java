package com.pedro.shorturl.repository;

import com.pedro.shorturl.domain.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UrlRepository extends MongoRepository<Url, String> {

    // Mágica do Spring: Ele vê "findByShortCode" e já sabe que tem que buscar
    // no campo 'shortCode' da coleção.
    Optional<Url> findByShortCode(String shortCode);
}