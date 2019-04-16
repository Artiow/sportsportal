package ru.vldf.sportsportal.service.general.throwable;

import java.util.function.Supplier;

public class ResourceNotFoundException extends AbstractResourceException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static Supplier<ResourceNotFoundException> supplier(String message) {
        return () -> new ResourceNotFoundException(message);
    }

    public static Supplier<ResourceNotFoundException> supplier(String message, Throwable cause) {
        return () -> new ResourceNotFoundException(message, cause);
    }
}
