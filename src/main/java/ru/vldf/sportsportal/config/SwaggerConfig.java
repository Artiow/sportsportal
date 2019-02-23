package ru.vldf.sportsportal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import ru.vldf.sportsportal.config.security.routing.RightsDifferentiationRouter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final static String CONTROLLER_PACKAGE = "ru.vldf.sportsportal.controller";

    private final static String JWT_AUTH_NAME = "JWT authorization";
    private final static String BASIC_AUTH_NAME = "Basic authorization";

    private final static String JWT_AUTH_DESCRIPTION = "JWT REST API authorization";
    private final static String BASIC_AUTH_DESCRIPTION = "Basic login authorization";

    private final String jwtAuthRegex;
    private final String basicAuthRegex;


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


    @Autowired
    public SwaggerConfig(RightsDifferentiationRouter router) {
        String loginPath = router.getLoginPath();
        this.jwtAuthRegex = String.format("^(?!%s).*", loginPath);
        this.basicAuthRegex = String.format("^%s", loginPath);
    }


    /**
     * Swagger configuration.
     *
     * @return docket bean.
     */
    @Bean
    public Docket api() {
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

    private List<SecurityScheme> securitySchemes() {
        return Arrays.asList(
                new ApiKey(JWT_AUTH_NAME, HttpHeaders.AUTHORIZATION, "header"),
                new BasicAuth(BASIC_AUTH_NAME)
        );
    }

    private List<SecurityContext> securityContexts() {
        return Arrays.asList(
                SecurityContext.builder().securityReferences(securityReference(JWT_AUTH_NAME, JWT_AUTH_DESCRIPTION)).forPaths(PathSelectors.regex(jwtAuthRegex)).build(),
                SecurityContext.builder().securityReferences(securityReference(BASIC_AUTH_NAME, BASIC_AUTH_DESCRIPTION)).forPaths(PathSelectors.regex(basicAuthRegex)).build()
        );
    }

    private List<SecurityReference> securityReference(String reference, String description) {
        return Collections.singletonList(
                new SecurityReference(reference, new AuthorizationScope[]{new AuthorizationScope("global", description)})
        );
    }
}
