package ru.vldf.sportsportal.service.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

/**
 * @author Namednev Artem
 */
public interface AuthorizationProvider {

    /**
     * Returns user details by username and password.
     *
     * @param username the username.
     * @param password the password.
     * @return user details.
     */
    IdentifiedUserDetails authorization(String username, String password) throws UsernameNotFoundException, BadCredentialsException;

    /**
     * Returns user details by access token.
     *
     * @param accessToken the access token.
     * @return user details.
     */
    IdentifiedUserDetails access(String accessToken) throws AuthenticationException;

    /**
     * Returns user details by refresh token.
     *
     * @param refreshToken the refresh token.
     * @return user details.
     */
    IdentifiedUserDetails refresh(String refreshToken) throws AuthenticationException;
}
