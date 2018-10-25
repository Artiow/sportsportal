package ru.vldf.sportsportal.service.security;

import org.springframework.data.util.Pair;

public interface SecurityProvider {

    /**
     * Returns pair (access and refresh) of token by user credentials.
     *
     * @param username {@link String} user username
     * @param password {@link String} user password
     * @return {@link Pair} pair (access and refresh) of token
     */
    Pair<String, String> authentication(String username, String password);

    /**
     * Returns pair (access and refresh) of token by refresh token.
     *
     * @param refreshToken {@link String} refresh token
     * @return {@link Pair} pair (access and refresh) of token
     */
    Pair<String, String> refresh(String refreshToken);

    /**
     * Logout user by access token.
     *
     * @param accessToken {@link String} access token
     */
    void logout(String accessToken);
}
