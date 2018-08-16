package ru.vldf.sportsportal.service.generic;

public class ForbiddenAccessException extends Exception {

    public ForbiddenAccessException(String message) {
        super(message);
    }

    public ForbiddenAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
