package com.pedro.shorturl.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ShortURL API")
                        .description("API escalável de encurtamento de URL com Redis e MongoDB.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Pedro")
                                .url("www.linkedin.com/in/pedrovitorribeirosilva")
                                .email("pedrovitorrbeirosilva@gmail.com")));
    }
}