package ru.vldf.sportsportal.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.vldf.sportsportal.config.MessagesConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private MessagesConfig messages;

    private String tokenType;


    /**
     * Required authentication request matcher setting.
     */
    protected TokenAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }


    @Autowired
    public void setMessages(MessagesConfig messages) {
        this.messages = messages;
    }

    @Value("${jwt.token-type}")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
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
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response

    ) throws AuthenticationException {

        final String token;
        final String credentials = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (credentials == null) {
            throw new AuthenticationCredentialsNotFoundException(messages.get("sportsportal.auth.filter.credentialsNotFound.message"));
        } else if (!credentials.startsWith(tokenType)) {
            throw new BadCredentialsException(messages.get("sportsportal.auth.filter.credentialsNotValid.message"));
        } else {
            token = credentials.substring(tokenType.length()).trim();
        }

        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(null, token));
    }

    /**
     * Actions configuration in case of successful authentication.
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain, Authentication authResult

    ) throws IOException, ServletException {

        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
