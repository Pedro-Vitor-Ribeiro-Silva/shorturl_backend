package com.pedro.shorturl.controller.dto;

public record ShortenUrlResponse(String originalUrl, String shortLink, String code) {
}