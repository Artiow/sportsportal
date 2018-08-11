package ru.vldf.sportsportal.service.generic;

public class ResourceFileNotFoundException extends Exception {

    public ResourceFileNotFoundException(String message) {
        super(message);
    }

    public ResourceFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
