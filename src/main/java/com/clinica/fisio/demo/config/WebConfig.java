package com.clinica.fisio.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Isso diz ao Spring: "Quando alguem acessar /uploads/**, procure nesta pasta"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/home/ramon/Documentos/clinica_fisio/uploads/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Centraliza a configuração do limite de CORS, evitando o @CrossOrigin repetitivo
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .exposedHeaders("Content-Disposition"); // Necessário para o download de exames
    }
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();

   }
}