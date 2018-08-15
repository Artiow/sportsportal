package ru.vldf.sportsportal.config.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public interface RightsDifferentiationRouting {

    RequestMatcher ADMIN_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/*", "POST"),
            new AntPathRequestMatcher("/api/*/*", "PUT"),
            new AntPathRequestMatcher("/api/*/*", "DELETE")
    );

    RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
            ADMIN_API_URLS
    );
}
