package ru.vldf.sportsportal.config.security;

import org.springframework.security.web.util.matcher.*;

public interface RightsDifferentiationRouting {

    RequestMatcher PUBLIC_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/auth/login", "GET"),
            new AntPathRequestMatcher("/auth/register", "POST")
    );

    RequestMatcher ADMIN_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/*/*", "POST"),
            new AntPathRequestMatcher("/*/*/*", "PUT"),
            new AntPathRequestMatcher("/*/*/*", "DELETE")
    );

    RequestMatcher USER_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/leaseapi/playground/*/reserve", "POST")
    );

    RequestMatcher PROTECTED_URLS = new AndRequestMatcher(
            new NegatedRequestMatcher(PUBLIC_API_URLS),
            new OrRequestMatcher(
                    ADMIN_API_URLS,
                    USER_API_URLS
            )
    );
}
