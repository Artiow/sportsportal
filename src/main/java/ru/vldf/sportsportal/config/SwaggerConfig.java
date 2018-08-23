package ru.vldf.sportsportal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static String AUTHORIZATION_NAME = "Token";

    @Value("${api.version}")
    private String apiVersion;

    /**
     * Swagger configuration.
     *
     * @return docket bean
     */
    @Bean
    public Docket api() {
        String CONTROLLER_PACKAGE = "ru.vldf.sportsportal.controller";

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        String TITLE = "SportsPortal Api";
        String DESCRIPTION = "SportsPortal by VLDF Api Documentation";
        String LICENCE = "Apache 2.0";
        String LICENCE_URL = "http://www.apache.org/licenses/LICENSE-2.0.html";
        Contact CONTACT = new Contact(
                "Artiow",
                "https://github.com/Artiow",
                "namednev_a@mail.ru"
        );

        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .contact(CONTACT)
                .license(LICENCE)
                .licenseUrl(LICENCE_URL)
                .version(apiVersion)
                .build();
    }

    private List<ApiKey> securitySchemes() {
        return Collections.singletonList(new ApiKey(AUTHORIZATION_NAME, HttpHeaders.AUTHORIZATION, "header"));
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder().securityReferences(defaultAuth(AUTHORIZATION_NAME)).forPaths(PathSelectors.regex("/.*")).build());
    }

    private List<SecurityReference> defaultAuth(String reference) {
        final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Collections.singletonList(new SecurityReference(reference, authorizationScopes));
    }
}
