package ru.vldf.sportsportal.config.security.components.jwt;

import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.security.components.AbstractTokenAuthenticationFilter;

/**
 * @author Namednev Artem
 */
public class JWTAuthenticationFilter extends AbstractTokenAuthenticationFilter<JWTAuthenticationToken> {

    public JWTAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher, "bearer");
    }

    @Override
    public JWTAuthenticationToken createFrom(String token) {
        return new JWTAuthenticationToken(token);
    }
}
