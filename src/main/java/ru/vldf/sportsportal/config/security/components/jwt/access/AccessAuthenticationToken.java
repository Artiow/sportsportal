package ru.vldf.sportsportal.config.security.components.jwt.access;

import ru.vldf.sportsportal.config.security.components.jwt.JWTAuthenticationToken;

/**
 * @author Namednev Artem
 */
public class AccessAuthenticationToken extends JWTAuthenticationToken {

    public AccessAuthenticationToken(String token) {
        super(token);
    }
}
