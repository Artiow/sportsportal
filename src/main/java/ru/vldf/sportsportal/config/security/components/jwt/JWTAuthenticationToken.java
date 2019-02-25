package ru.vldf.sportsportal.config.security.components.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

/**
 * @author Namednev Artem
 */
public abstract class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public JWTAuthenticationToken(String token) {
        super(null, token);
    }

    public String token() {
        return Optional.ofNullable(getCredentials()).map(Object::toString).orElse(null);
    }
}
