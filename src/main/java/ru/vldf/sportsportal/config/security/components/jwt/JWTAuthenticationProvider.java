package ru.vldf.sportsportal.config.security.components.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vldf.sportsportal.config.security.components.AbstractTokenAuthenticationProvider;

/**
 * @author Namednev Artem
 */
public class JWTAuthenticationProvider extends AbstractTokenAuthenticationProvider<JWTAuthenticationToken> {

    public JWTAuthenticationProvider() {
        super(JWTAuthenticationToken.class);
    }

    @Override
    protected UserDetails retrieveUser(JWTAuthenticationToken auth) throws AuthenticationException {
        return provider.authorization(auth.token());
    }
}
