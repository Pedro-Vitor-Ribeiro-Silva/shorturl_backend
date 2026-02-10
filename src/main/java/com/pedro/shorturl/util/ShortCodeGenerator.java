package com.pedro.shorturl.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class ShortCodeGenerator {

    // Caracteres permitidos: A-Z, a-z, 0-9 (Base62)
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private final Random random = new SecureRandom(); // Mais seguro que Random comum

    public String generate() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            // Sorteia um índice aleatório da string ALPHABET
            int randomIndex = random.nextInt(ALPHABET.length());
            // Pega o caractere desse índice e adiciona ao código
            sb.append(ALPHABET.charAt(randomIndex));
        }
        return sb.toString();
    }
}