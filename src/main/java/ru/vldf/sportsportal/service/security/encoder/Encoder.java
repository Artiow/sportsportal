package ru.vldf.sportsportal.service.security.encoder;

import java.util.Map;

public interface Encoder {

    /**
     * Generate access token.
     *
     * @param payload {@link Map} token payload
     * @return {@link String} access token
     */
    String getAccessToken(final Map<String, Object> payload);

    /**
     * Generate refresh token.
     *
     * @param payload {@link Map} token payload
     * @return {@link String} refresh token
     */
    String getRefreshToken(final Map<String, Object> payload);

    /**
     * Returns payload of parsed token.
     *
     * @param token {@link String} token
     * @return map of token payload
     */
    Map<String, Object> verify(final String token);
}
