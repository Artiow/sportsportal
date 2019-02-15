package ru.vldf.sportsportal.service.security;

import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

/**
 * @author Namednev Artem
 */
public interface AuthorizationProvider {

    /**
     * Returns IdentifiedUserDetails by access token.
     *
     * @param accessToken {@link String} access token
     * @return {@link IdentifiedUserDetails} user details
     */
    IdentifiedUserDetails authorization(String accessToken);
}
