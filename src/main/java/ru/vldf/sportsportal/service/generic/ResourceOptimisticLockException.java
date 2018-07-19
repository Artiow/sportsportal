package ru.vldf.sportsportal.service.generic;

public class ResourceOptimisticLockException extends Exception {

    public ResourceOptimisticLockException(String message) {
        super(message);
    }

    public ResourceOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
