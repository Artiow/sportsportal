package ru.vldf.sportsportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .exposedHeaders(HttpHeaders.LOCATION)
                .allowedMethods("*")
                .allowedOrigins("*");
    }

    @Bean
    public RestOperations getRestOperations() {
        return new RestTemplate();
    }
}
