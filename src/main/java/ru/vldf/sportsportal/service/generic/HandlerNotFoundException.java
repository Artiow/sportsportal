package ru.vldf.sportsportal.service.generic;

public class HandlerNotFoundException extends Exception {

    public HandlerNotFoundException(String message) {
        super(message);
    }

    public HandlerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
