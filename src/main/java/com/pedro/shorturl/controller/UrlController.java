package com.pedro.shorturl.controller;

import com.pedro.shorturl.controller.dto.ShortenUrlRequest;
import com.pedro.shorturl.controller.dto.ShortenUrlResponse;
import com.pedro.shorturl.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Url Shortener", description = "Endpoints para gerenciar e acessar URLs encurtadas")
public class UrlController {

    private final UrlService urlService;

    @Operation(summary = "Encurtar URL", description = "Recebe uma URL original e retorna o link encurtado")
    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request, HttpServletRequest servletRequest) {

        String code = urlService.shortenUrl(request.url());

        // Monta a URL completa (ex: http://localhost:8080/código)
        String redirectUrl = servletRequest.getRequestURL().toString().replace("/shorten", "/" + code);

        ShortenUrlResponse response = new ShortenUrlResponse(
                request.url(),
                redirectUrl,
                code
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Redirecionar", description = "Recebe o código, busca a URL original e redireciona o usuário")
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        String originalUrl = urlService.getOriginalUrl(code);

        // HTTP 302: Found (Redirecionamento temporário)
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}