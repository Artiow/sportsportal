package ru.vldf.sportsportal.service.generic;

public class ResourceCannotCreateException extends Exception {

    public ResourceCannotCreateException(String message) {
        super(message);
    }

    public ResourceCannotCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
