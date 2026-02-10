package com.pedro.shorturl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera para todas as rotas
                .allowedOrigins("*") // Em produção, trocar "*" pelo domínio do seu front (ex: https://meusite.com)
                .allowedMethods("GET", "POST", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*");
    }
}