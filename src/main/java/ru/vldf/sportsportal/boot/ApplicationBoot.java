package ru.vldf.sportsportal.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan({
        "ru.vldf.sportsportal.domain"
})
@EnableJpaRepositories({
        "ru.vldf.sportsportal.repository"
})
@ComponentScan({
        "ru.vldf.sportsportal.dto",
        "ru.vldf.sportsportal.mapper",
        "ru.vldf.sportsportal.config",
        "ru.vldf.sportsportal.service",
        "ru.vldf.sportsportal.controller",
        "ru.vldf.sportsportal.integration"
})
@SpringBootApplication(exclude = {
        ThymeleafAutoConfiguration.class
})
@EnableTransactionManagement
public class ApplicationBoot extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBoot.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApplicationBoot.class);
    }
}
