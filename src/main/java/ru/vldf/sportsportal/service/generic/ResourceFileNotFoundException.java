package ru.vldf.sportsportal.service.generic;

public class ResourceFileNotFoundException extends AbstractResourceException {

    public ResourceFileNotFoundException(String message) {
        super(message);
    }

    public ResourceFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
