package ru.vldf.sportsportal.service.security.keykeeper;

import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

/**
 * @author Namednev Artem
 */
public interface KeyProvider {

    /**
     * Returns user details by user credentials.
     *
     * @param username the user username.
     * @param password the user password.
     * @return user details.
     */
    IdentifiedUserDetails authorization(String username, String password) throws UsernameNotFoundException, BadCredentialsException;

    /**
     * Returns user details by access token payload.
     *
     * @param accessKey the access token payload.
     * @return user details.
     */
    IdentifiedUserDetails authorization(Payload accessKey) throws UsernameNotFoundException;

    /**
     * Returns token payload pair (access and refresh) by user credentials.
     *
     * @param username the user username.
     * @param password the user password.
     * @return token payload pair (access and refresh).
     */
    Pair<Payload, Payload> access(String username, String password) throws UsernameNotFoundException, BadCredentialsException;

    /**
     * Returns token payload pair (access and refresh) by refresh token payload.
     *
     * @param refreshKey the refresh token payload.
     * @return token payload pair (access and refresh).
     */
    Pair<Payload, Payload> refresh(Payload refreshKey) throws UsernameNotFoundException;
}
