package ru.vldf.sportsportal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Namednev Artem
 */
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Value("${server.upload-size.max:1048576}")
    private long maxUploadSize;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .exposedHeaders(HttpHeaders.LOCATION)
                .allowedMethods("*")
                .allowedOrigins("*");
    }


    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(maxUploadSize);
        return multipartResolver;
    }
}
