package ru.vldf.sportsportal.config.security;

import org.springframework.security.web.util.matcher.*;

public interface RightsDifferentiationRouting {

    RequestMatcher DEFAULT_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/favicon.ico"),
            new AntPathRequestMatcher("/error"),
            new AntPathRequestMatcher("/csrf"),
            new AntPathRequestMatcher("/")
    );

    RequestMatcher SWAGGER_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/configuration/**"),
            new AntPathRequestMatcher("/webjars/**"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/v2/api-docs")
    );

    RequestMatcher PUBLIC_API_URLS = new AndRequestMatcher(
            new AntPathRequestMatcher("/api/**")
    );

    RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            PUBLIC_API_URLS,
            SWAGGER_URLS,
            DEFAULT_URLS
    );

    RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(
            PUBLIC_URLS
    );
}
