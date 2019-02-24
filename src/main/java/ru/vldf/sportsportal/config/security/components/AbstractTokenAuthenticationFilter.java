package ru.vldf.sportsportal.config.security.components;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import ru.vldf.sportsportal.config.messages.MessageContainer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Namednev Artem
 */
public abstract class AbstractTokenAuthenticationFilter<T extends UsernamePasswordAuthenticationToken> extends AbstractAuthenticationProcessingFilter {

    private final String schema;

    protected MessageContainer messageContainer;


    /**
     * Abstract token authentication filter constructor.
     *
     * @param requiresAuthenticationRequestMatcher the request matcher that define filtered paths.
     * @param schema                               the authorization schema.
     */
    public AbstractTokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, String schema) {
        super(requiresAuthenticationRequestMatcher);
        Assert.hasText(schema, "Authorization schema cannot be null");
        this.schema = (schema.toLowerCase().trim() + " ");
    }


    public void setMessageContainer(MessageContainer messageContainer) {
        Assert.notNull(messageContainer, "Message container cannot be null");
        this.messageContainer = messageContainer;
        this.messages = messageContainer.getAccessor();
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(messageContainer, "Message container must be specified");
        Assert.notNull(messages, "Message source accessor must be specified");
    }


    /**
     * Authentication token creating from extracted token.
     *
     * @param token the extracted token.
     * @return authentication token.
     * @throws AuthenticationException if accepted token invalid.
     */
    public abstract T createFrom(String token) throws AuthenticationException;

    /**
     * Authenticate user by http token.
     *
     * @param request  the http request.
     * @param response the http response.
     * @return authenticated token.
     * @throws AuthenticationException if accepted token invalid.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String credentials;
        if ((credentials = request.getHeader(HttpHeaders.AUTHORIZATION)) == null) {
            throw new AuthenticationCredentialsNotFoundException(messageContainer.get("sportsportal.auth.filter.credentialsNotFound.message"));
        } else if (!credentials.toLowerCase().startsWith(schema)) {
            throw new BadCredentialsException(messageContainer.get("sportsportal.auth.filter.credentialsNotValid.message"));
        } else {
            return getAuthenticationManager().authenticate(createFrom(credentials.substring(schema.length()).trim()));
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
