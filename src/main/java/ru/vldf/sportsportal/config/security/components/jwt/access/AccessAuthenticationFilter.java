package ru.vldf.sportsportal.config.security.components.jwt.access;

import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.security.components.jwt.JWTAuthenticationFilter;

/**
 * @author Namednev Artem
 */
public class AccessAuthenticationFilter extends JWTAuthenticationFilter<AccessAuthenticationToken> {

    public AccessAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public AccessAuthenticationToken createFrom(String token) {
        return new AccessAuthenticationToken(token);
    }
}
