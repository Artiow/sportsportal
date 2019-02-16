package ru.vldf.sportsportal.service.security;

import org.springframework.data.util.Pair;

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
    Pair<String, String> authentication(String username, String password);

    /**
     * Returns token pair (access and refresh) by refresh token.
     *
     * @param refreshToken the refresh token.
     * @return token pair (access and refresh).
     */
    Pair<String, String> refresh(String refreshToken);

    /**
     * Logout user by access token.
     *
     * @param accessToken the access token.
     */
    void logout(String accessToken);

    /**
     * Logout all user sessions by access token.
     *
     * @param accessToken the access token.
     */
    void logoutAll(String accessToken);
}
