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

    @Value("${api.host}")
    private String apiHost;

    @Value("${api.version}")
    private String apiVersion;

    @Value("${api.title}")
    private String apiTitle;

    @Value("${api.description}")
    private String apiDescription;

    @Value("${api.licence.name}")
    private String apiLicenceName;

    @Value("${api.licence.url}")
    private String apiLicenceUrl;

    @Value("${api.contact.name}")
    private String apiContactName;

    @Value("${api.contact.url}")
    private String apiContactUrl;

    @Value("${api.contact.email}")
    private String apiContactEmail;

    /**
     * Swagger configuration.
     *
     * @return docket bean
     */
    @Bean
    public Docket api() {
        String CONTROLLER_PACKAGE = "ru.vldf.sportsportal.controller";

        return new Docket(DocumentationType.SWAGGER_2)
                .host(apiHost)
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(apiTitle)
                .version(apiVersion)
                .description(apiDescription)
                .license(apiLicenceName)
                .licenseUrl(apiLicenceUrl)
                .contact(new Contact(
                        apiContactName,
                        apiContactUrl,
                        apiContactEmail
                )).build();
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
