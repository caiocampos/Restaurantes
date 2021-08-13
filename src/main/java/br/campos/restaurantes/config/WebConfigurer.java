package br.campos.restaurantes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
			.allowedMethods("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
			.allowedHeaders("Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "Access-Control-Request-Method",
                "Access-Control-Request-Headers", "Origin", "Cache-Control", "Content-Type", "Authorization")
            .allowCredentials(true);
    }
}