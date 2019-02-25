package ru.vldf.sportsportal.config.security.components.jwt.refresh;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vldf.sportsportal.config.security.components.jwt.JWTAuthenticationProvider;

/**
 * @author Namednev Artem
 */
public class RefreshAuthenticationProvider extends JWTAuthenticationProvider<RefreshAuthenticationToken> {

    public RefreshAuthenticationProvider() {
        super(RefreshAuthenticationToken.class);
    }

    @Override
    protected UserDetails retrieveUser(String refreshToken) throws AuthenticationException {
        return provider.refresh(refreshToken);
    }
}
