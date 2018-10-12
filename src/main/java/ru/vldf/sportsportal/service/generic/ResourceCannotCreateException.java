package ru.vldf.sportsportal.service.generic;

public class ResourceCannotCreateException extends AbstractResourceException {

    public ResourceCannotCreateException(String message) {
        super(message);
    }

    public ResourceCannotCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
