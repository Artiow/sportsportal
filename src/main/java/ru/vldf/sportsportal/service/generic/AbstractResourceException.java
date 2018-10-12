package ru.vldf.sportsportal.service.generic;

public abstract class AbstractResourceException extends Exception {

    public AbstractResourceException(String message) {
        super(message);
    }

    public AbstractResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
