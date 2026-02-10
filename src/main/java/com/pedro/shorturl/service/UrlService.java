package com.pedro.shorturl.service;

import com.pedro.shorturl.domain.model.Url;
import com.pedro.shorturl.repository.UrlRepository;
import com.pedro.shorturl.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor // Cria o construtor automaticamente para injeção de dependência
public class UrlService {

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final StringRedisTemplate redisTemplate; // Redis otimizado para String

    // Constante para o tempo de vida do cache (ex: 24 horas)
    private static final long CACHE_TTL_HOURS = 24;

    public String shortenUrl(String originalUrl) {

        // Se a URL não começar com http ou https, adiciona https:// por padrão
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        String code;

        // Loop de segurança para colisão (muito raro, mas possível)
        do {
            code = shortCodeGenerator.generate();
        } while (urlRepository.findByShortCode(code).isPresent());

        // 1. Salva no MongoDB (Persistência)
        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortCode(code)
                .createdAt(LocalDateTime.now())
                .build();
        urlRepository.save(url);

        // 2. Salva no Redis (Cache) com expiração
        redisTemplate.opsForValue().set(code, originalUrl, CACHE_TTL_HOURS, TimeUnit.HOURS);

        return code;
    }

    public String getOriginalUrl(String shortCode) {
        // 1. Tenta pegar do Redis (MUITO RÁPIDO)
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        // 2. Se não tiver no Redis, busca no MongoDB (Mais lento)
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        // 3. Salva no Redis para a próxima vez ser rápida
        redisTemplate.opsForValue().set(shortCode, url.getOriginalUrl(), CACHE_TTL_HOURS, TimeUnit.HOURS);

        return url.getOriginalUrl();
    }
}