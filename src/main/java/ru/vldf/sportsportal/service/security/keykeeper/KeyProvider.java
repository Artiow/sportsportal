package ru.vldf.sportsportal.service.security.keykeeper;

import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

public interface KeyProvider {

    /**
     * Returns pair (access and refresh) of token payload by user credentials.
     *
     * @param username {@link String} user username
     * @param password {@link String} user password
     * @return {@link Pair} pair (access and refresh) of token payload
     * @throws UsernameNotFoundException if user not found
     */
    Pair<Payload, Payload> authentication(String username, String password);

    /**
     * Returns IdentifiedUserDetails by access token payload.
     *
     * @param accessKey {@link Payload} access token payload
     * @return {@link IdentifiedUserDetails} user details
     */
    IdentifiedUserDetails authorization(Payload accessKey);

    /**
     * Returns pair (access and refresh) of token payload by refresh token payload.
     *
     * @param refreshKey {@link Payload} refresh token payload
     * @return {@link Pair} pair (access and refresh) of token payload
     */
    Pair<Payload, Payload> refresh(Payload refreshKey);

    /**
     * Logout user by access token payload.
     *
     * @param accessKey {@link Payload} access token payload
     */
    void logout(Payload accessKey);
}
