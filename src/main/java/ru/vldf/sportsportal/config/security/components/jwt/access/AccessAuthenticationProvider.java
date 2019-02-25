package ru.vldf.sportsportal.config.security.components.jwt.access;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vldf.sportsportal.config.security.components.jwt.JWTAuthenticationProvider;

/**
 * @author Namednev Artem
 */
public class AccessAuthenticationProvider extends JWTAuthenticationProvider<AccessAuthenticationToken> {

    public AccessAuthenticationProvider() {
        super(AccessAuthenticationToken.class);
    }

    @Override
    protected UserDetails retrieveUser(String accessToken) throws AuthenticationException {
        return provider.access(accessToken);
    }
}
