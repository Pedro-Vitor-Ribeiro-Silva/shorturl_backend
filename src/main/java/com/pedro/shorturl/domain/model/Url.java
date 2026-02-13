package com.pedro.shorturl.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "urls") // Diz ao Mongo que isso é uma "tabela" (coleção)
@Data // Cria Getters, Setters, toString, etc (Lombok)
@Builder // Ajuda a criar objetos de forma fluida
@NoArgsConstructor // Construtor vazio
@AllArgsConstructor // Construtor com tudo
public class Url {

    @Id
    private String id; // Mongo usa String como ID por padrão (aquele hash louco)
    @Indexed
    private String originalUrl; // A URL grande (ex: https://google.com/search?...)

    @Indexed(unique = true) // Garante que o código curto seja ÚNICO no banco
    private String shortCode; // O código gerado (ex: aB3dE)

    private LocalDateTime createdAt; // Data de criação para sabermos quando expirar
}