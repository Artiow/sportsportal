package ru.vldf.sportsportal.service.general.throwable;

public abstract class AbstractAuthorizationException extends Exception {

    public AbstractAuthorizationException(String message) {
        super(message);
    }

    public AbstractAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
