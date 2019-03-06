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

    private final static String BASIC_AUTH_NAME = "Basic authorization";
    private final static String ACCESS_AUTH_NAME = "JWT access authorization";
    private final static String REFRESH_AUTH_NAME = "JWT refresh authorization";

    private final static String BASIC_AUTH_DESCRIPTION = "Basic login authorization";
    private final static String ACCESS_AUTH_DESCRIPTION = "JWT access REST API authorization";
    private final static String REFRESH_AUTH_DESCRIPTION = "JWT refresh REST API authorization";

    private final String basicAuthRegex;
    private final String accessAuthRegex;
    private final String refreshAuthRegex;


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
        String refreshPath = router.getRefreshPath();
        this.basicAuthRegex = String.format("^%s", loginPath);
        this.refreshAuthRegex = String.format("^%s", refreshPath);
        this.accessAuthRegex = String.format("^(?!%s|%s).*", loginPath, refreshPath);
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
                new ApiKey(ACCESS_AUTH_NAME, HttpHeaders.AUTHORIZATION, "header"),
                new ApiKey(REFRESH_AUTH_NAME, HttpHeaders.AUTHORIZATION, "header"),
                new BasicAuth(BASIC_AUTH_NAME)
        );
    }

    private List<SecurityContext> securityContexts() {
        return Arrays.asList(
                securityContext(BASIC_AUTH_NAME, BASIC_AUTH_DESCRIPTION, basicAuthRegex),
                securityContext(ACCESS_AUTH_NAME, ACCESS_AUTH_DESCRIPTION, accessAuthRegex),
                securityContext(REFRESH_AUTH_NAME, REFRESH_AUTH_DESCRIPTION, refreshAuthRegex)
        );
    }

    private SecurityContext securityContext(String name, String description, String pathRegex) {
        return SecurityContext.builder().securityReferences(securityReference(name, description)).forPaths(PathSelectors.regex(pathRegex)).build();
    }

    private List<SecurityReference> securityReference(String reference, String description) {
        return Collections.singletonList(
                new SecurityReference(reference, new AuthorizationScope[]{new AuthorizationScope("global", description)})
        );
    }
}
