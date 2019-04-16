package ru.vldf.sportsportal.service.general.throwable;

public class ResourceOptimisticLockException extends AbstractResourceException {

    public ResourceOptimisticLockException(String message) {
        super(message);
    }

    public ResourceOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
