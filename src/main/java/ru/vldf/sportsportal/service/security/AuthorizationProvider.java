package ru.vldf.sportsportal.service.security;

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
    IdentifiedUserDetails authorization(String username, String password);

    /**
     * Returns user details by access token.
     *
     * @param accessToken the access token.
     * @return user details.
     */
    IdentifiedUserDetails authorization(String accessToken);
}
