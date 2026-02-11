package com.pedro.shorturl.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig implements WebMvcConfigurer {
    private final RateLimitInterceptor interceptor;

    public RateLimitConfig(RateLimitInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/shorten");
    }
}

@Component
class RateLimitInterceptor implements HandlerInterceptor {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Value("${app.rate-limit.capacity}")
    private int capacity;
    @Value("${app.rate-limit.tokens}")
    private int tokens;
    @Value("${app.rate-limit.minutes}")
    private int minutes;

    private Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, k -> {
            Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(tokens, Duration.ofMinutes(minutes)));
            return Bucket.builder().addLimit(limit).build();
        });
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (resolveBucket(request.getRemoteAddr()).tryConsume(1)) {
            return true;
        } else {
            response.setStatus(429);
            response.getWriter().write("Muitas requisicoes! Aguarde.");
            return false;
        }
    }
}