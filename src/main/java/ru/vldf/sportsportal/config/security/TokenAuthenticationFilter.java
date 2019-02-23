package ru.vldf.sportsportal.config.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.messages.MessageContainer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Namednev Artem
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public final static String AUTHORIZATION_SCHEMA = "Bearer";

    private final MessageContainer messages;


    /**
     * Required authentication request matcher setting.
     *
     * @param requiresAuthenticationRequestMatcher {@link RequestMatcher}
     * @param messages                             {@link MessageContainer}
     */
    public TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, MessageContainer messages) {
        super(requiresAuthenticationRequestMatcher);
        this.messages = messages;
    }


    /**
     * Returns user authentication in case of successful authentication.
     *
     * @param request  - http request
     * @param response - http response
     * @return authentication
     * @throws AuthenticationException - if valid token not found in request
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String credentials;
        if ((credentials = request.getHeader(HttpHeaders.AUTHORIZATION)) == null) {
            throw new AuthenticationCredentialsNotFoundException(messages.get("sportsportal.auth.filter.credentialsNotFound.message"));
        } else if (!credentials.startsWith(AUTHORIZATION_SCHEMA)) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.filter.credentialsNotValid.message"));
        } else {
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(null, credentials.substring(AUTHORIZATION_SCHEMA.length()).trim()));
        }
    }

    /**
     * Actions configuration in case of successful authentication.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
