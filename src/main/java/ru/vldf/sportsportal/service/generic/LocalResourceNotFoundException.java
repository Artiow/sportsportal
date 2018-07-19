package ru.vldf.sportsportal.service.generic;

public class LocalResourceNotFoundException extends Exception {

    public LocalResourceNotFoundException(String message) {
        super(message);
    }

    public LocalResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
