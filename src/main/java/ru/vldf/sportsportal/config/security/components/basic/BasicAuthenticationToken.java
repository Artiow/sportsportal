package ru.vldf.sportsportal.config.security.components.basic;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Objects;

/**
 * @author Namednev Artem
 */
public class BasicAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public BasicAuthenticationToken(String username, String password) {
        super(username, password);
    }

    public String username() {
        return Objects.toString(getPrincipal(), null);
    }

    public String password() {
        return Objects.toString(getCredentials(), null);
    }
}
