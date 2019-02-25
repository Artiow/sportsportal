package ru.vldf.sportsportal.config.security.components.jwt.refresh;

import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.security.components.jwt.JWTAuthenticationFilter;

/**
 * @author Namednev Artem
 */
public class RefreshAuthenticationFilter extends JWTAuthenticationFilter<RefreshAuthenticationToken> {

    public RefreshAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public RefreshAuthenticationToken createFrom(String token) {
        return new RefreshAuthenticationToken(token);
    }
}
