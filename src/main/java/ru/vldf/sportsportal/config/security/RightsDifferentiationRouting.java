package ru.vldf.sportsportal.config.security;

import org.springframework.security.web.util.matcher.*;

public interface RightsDifferentiationRouting {

    RequestMatcher PUBLIC_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/auth/login", "GET"),
            new AntPathRequestMatcher("/api/auth/register", "POST")
    );

    RequestMatcher ADMIN_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/*", "POST"),
            new AntPathRequestMatcher("/api/*/*", "PUT"),
            new AntPathRequestMatcher("/api/*/*", "DELETE"),
            new AntPathRequestMatcher("/api/lease/*", "POST"),
            new AntPathRequestMatcher("/api/lease/*/*", "PUT"),
            new AntPathRequestMatcher("/api/lease/*/*", "DELETE")
    );

    RequestMatcher USER_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/user/*", "GET"),
            new AntPathRequestMatcher("/api/lease/**/reserve", "POST")
    );

    RequestMatcher PROTECTED_URLS = new AndRequestMatcher(
            new NegatedRequestMatcher(PUBLIC_API_URLS),
            new OrRequestMatcher(
                    ADMIN_API_URLS,
                    USER_API_URLS
            )
    );
}
