package com.pedro.shorturl.service;

import com.pedro.shorturl.domain.model.Url;
import com.pedro.shorturl.repository.UrlRepository;
import com.pedro.shorturl.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.cache.ttl-hours}")
    private long cacheTtlHours;

    public String shortenUrl(String originalUrl) {
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        String code;
        do {
            code = shortCodeGenerator.generate();
        } while (urlRepository.findByShortCode(code).isPresent());

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortCode(code)
                .createdAt(LocalDateTime.now())
                .build();
        urlRepository.save(url);

        redisTemplate.opsForValue().set(code, originalUrl, cacheTtlHours, TimeUnit.HOURS);
        return code;
    }

    public String getOriginalUrl(String shortCode) {
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);
        if (cachedUrl != null) return cachedUrl;

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        redisTemplate.opsForValue().set(shortCode, url.getOriginalUrl(), cacheTtlHours, TimeUnit.HOURS);
        return url.getOriginalUrl();
    }
}