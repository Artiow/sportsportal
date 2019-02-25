package ru.vldf.sportsportal.config.security.components.jwt;

import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.security.components.AbstractTokenAuthenticationFilter;

/**
 * @author Namednev Artem
 */
public abstract class JWTAuthenticationFilter<T extends JWTAuthenticationToken> extends AbstractTokenAuthenticationFilter<T> {

    public JWTAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher, "bearer");
    }
}
