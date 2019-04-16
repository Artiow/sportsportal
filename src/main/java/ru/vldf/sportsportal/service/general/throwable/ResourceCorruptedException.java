package ru.vldf.sportsportal.service.general.throwable;

public class ResourceCorruptedException extends AbstractResourceException {

    public ResourceCorruptedException(String message) {
        super(message);
    }

    public ResourceCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
