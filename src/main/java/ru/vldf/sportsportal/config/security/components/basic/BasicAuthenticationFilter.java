package ru.vldf.sportsportal.config.security.components.basic;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.security.components.AbstractTokenAuthenticationFilter;
import ru.vldf.sportsportal.util.models.Base64Credentials;

/**
 * @author Namednev Artem
 */
public class BasicAuthenticationFilter extends AbstractTokenAuthenticationFilter<BasicAuthenticationToken> {

    public BasicAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher, "basic");
    }

    @Override
    public BasicAuthenticationToken createFrom(String token) {
        try {
            Base64Credentials credentials = Base64Credentials.decode(token);
            return new BasicAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(messageContainer.get("sportsportal.auth.provider.couldNotParseToken.message"), e);
        }
    }
}
