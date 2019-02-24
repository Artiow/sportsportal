package ru.vldf.sportsportal.service.security;

import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Namednev Artem
 */
public interface SecurityProvider {

    /**
     * Returns token pair (access and refresh) by user credentials.
     *
     * @param username the user username.
     * @param password the user password.
     * @return token pair (access and refresh).
     */
    Pair<String, String> access(String username, String password) throws UsernameNotFoundException, BadCredentialsException;

    /**
     * Returns token pair (access and refresh) by refresh token.
     *
     * @param refreshToken the refresh token.
     * @return token pair (access and refresh).
     */
    Pair<String, String> refresh(String refreshToken) throws UsernameNotFoundException;
}
