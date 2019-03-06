package ru.vldf.sportsportal.service.security;

import org.springframework.data.util.Pair;

/**
 * @author Namednev Artem
 */
public interface SecurityProvider {

    /**
     * Returns generated token pair (access and refresh) by user identifier.
     *
     * @param userId the user identifier.
     * @return token pair (access and refresh).
     */
    Pair<String, String> generate(Integer userId);
}
