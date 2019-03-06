package ru.vldf.sportsportal.config.security.components.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Objects;

/**
 * @author Namednev Artem
 */
public abstract class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public JWTAuthenticationToken(String token) {
        super(null, token);
    }

    public String token() {
        return Objects.toString(getCredentials(), null);
    }
}
