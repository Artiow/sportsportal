package ru.vldf.sportsportal.config.security.components.basic;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vldf.sportsportal.config.security.components.AbstractTokenAuthenticationProvider;

/**
 * @author Namednev Artem
 */
public class BasicAuthenticationProvider extends AbstractTokenAuthenticationProvider<BasicAuthenticationToken> {

    public BasicAuthenticationProvider() {
        super(BasicAuthenticationToken.class);
    }

    @Override
    protected UserDetails retrieveUser(BasicAuthenticationToken auth) throws AuthenticationException {
        return provider.authorization(auth.username(), auth.password());
    }
}
