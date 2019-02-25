package ru.vldf.sportsportal.config.security.components.jwt.refresh;

import ru.vldf.sportsportal.config.security.components.jwt.JWTAuthenticationToken;

/**
 * @author Namednev Artem
 */
public class RefreshAuthenticationToken extends JWTAuthenticationToken {

    public RefreshAuthenticationToken(String token) {
        super(token);
    }
}
