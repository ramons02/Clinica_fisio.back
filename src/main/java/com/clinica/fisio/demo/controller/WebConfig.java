package com.clinica.fisio.demo.controller;

import org.springframework.context.annotation.Configuration;
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
}
