package ru.vldf.sportsportal.service.general.throwable;

public class UnauthorizedAccessException extends AbstractAuthorizationException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
