package ru.vldf.sportsportal.service.generic;

public class ResourceCannotUpdateException extends AbstractResourceException {

    public ResourceCannotUpdateException(String message) {
        super(message);
    }

    public ResourceCannotUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
