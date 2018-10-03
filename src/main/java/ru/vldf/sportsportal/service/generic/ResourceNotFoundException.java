package ru.vldf.sportsportal.service.generic;

public class ResourceNotFoundException extends AbstractResourceException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
