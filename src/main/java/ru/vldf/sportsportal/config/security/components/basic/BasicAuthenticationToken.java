package ru.vldf.sportsportal.config.security.components.basic;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

/**
 * @author Namednev Artem
 */
public class BasicAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public BasicAuthenticationToken(String username, String password) {
        super(username, password);
    }

    public String username() {
        return Optional.ofNullable(getPrincipal()).map(Object::toString).orElse(null);
    }

    public String password() {
        return Optional.ofNullable(getCredentials()).map(Object::toString).orElse(null);
    }
}
