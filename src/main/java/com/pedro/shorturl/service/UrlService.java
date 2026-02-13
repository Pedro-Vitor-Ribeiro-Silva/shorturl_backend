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
        // 1. Sanitização (Mantenha igual)
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        // 2. VERIFICAÇÃO: Se já existe, retorna o código existente
        // Isso evita duplicidade no MongoDB e economiza espaço
        var existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            String existingCode = existingUrl.get().getShortCode();
            // Garante que esteja no Redis (caso tenha expirado do cache mas não do banco)
            redisTemplate.opsForValue().set(existingCode, originalUrl, cacheTtlHours, TimeUnit.HOURS);
            return existingCode;
        }

        // 3. Se não existe, cria um novo (Lógica antiga continua aqui)
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