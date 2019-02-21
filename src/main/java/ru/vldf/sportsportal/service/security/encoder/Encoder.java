package ru.vldf.sportsportal.service.security.encoder;

import java.util.Map;

/**
 * @author Namednev Artem
 */
public interface Encoder {

    /**
     * Generate access token.
     *
     * @param payload the token payload.
     * @return access token.
     */
    String getAccessToken(final Map<String, Object> payload);

    /**
     * Generate refresh token.
     *
     * @param payload the token payload.
     * @return refresh token.
     */
    String getRefreshToken(final Map<String, Object> payload);

    /**
     * Returns payload of parsed token.
     *
     * @param token the token.
     * @return token payload.
     */
    Map<String, Object> verify(final String token);
}
