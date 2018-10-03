package ru.vldf.sportsportal.service.generic;

public abstract class AbstractAuthorizationException extends Exception {

    public AbstractAuthorizationException(String message) {
        super(message);
    }

    public AbstractAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
