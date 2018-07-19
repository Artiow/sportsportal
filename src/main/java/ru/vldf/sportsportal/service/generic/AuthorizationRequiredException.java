package ru.vldf.sportsportal.service.generic;

public class AuthorizationRequiredException extends Exception {

    public AuthorizationRequiredException(String message) {
        super(message);
    }

    public AuthorizationRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
