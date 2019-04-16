package ru.vldf.sportsportal.service.general.throwable;

public class ResourceCannotCreateException extends AbstractResourceException {

    public ResourceCannotCreateException(String message) {
        super(message);
    }

    public ResourceCannotCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
