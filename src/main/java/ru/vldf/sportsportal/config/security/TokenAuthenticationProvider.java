package ru.vldf.sportsportal.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.service.security.AuthorizationProvider;

/**
 * @author Namednev Artem
 */
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final AuthorizationProvider provider;
    private final MessageContainer messages;


    @Autowired
    public TokenAuthenticationProvider(AuthorizationProvider provider, MessageContainer messages) {
        this.provider = provider;
        this.messages = messages;
    }


    /**
     * Additional authentication checks.
     *
     * @param user - authenticated user
     * @param auth - authentication token
     * @throws AuthenticationException - if check fails
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails user, UsernamePasswordAuthenticationToken auth) throws AuthenticationException {

    }

    /**
     * Finding user by its authentication token.
     *
     * @param username - authentication username
     * @param auth     - authentication token
     * @return authenticated user
     * @throws AuthenticationException - if authentication fails
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken auth) throws AuthenticationException {
        try {
            return provider.authorization(auth.getCredentials().toString());
        } catch (SignatureException e) {
            throw new InsufficientAuthenticationException(messages.get("sportsportal.auth.provider.insufficientToken.message"), e);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException(messages.get("sportsportal.auth.provider.expiredToken.message"), e);
        } catch (JwtException e) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.provider.couldNotParseToken.message"), e);
        }
    }
}
