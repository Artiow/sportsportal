package ru.vldf.sportsportal.config.security.components.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vldf.sportsportal.config.security.components.AbstractTokenAuthenticationProvider;

/**
 * @author Namednev Artem
 */
public abstract class JWTAuthenticationProvider<T extends JWTAuthenticationToken> extends AbstractTokenAuthenticationProvider<T> {

    public JWTAuthenticationProvider(Class<T> type) {
        super(type);
    }

    @Override
    protected UserDetails retrieveUser(JWTAuthenticationToken auth) throws AuthenticationException {
        return retrieveUser(auth.token());
    }

    protected abstract UserDetails retrieveUser(String token) throws AuthenticationException;
}
