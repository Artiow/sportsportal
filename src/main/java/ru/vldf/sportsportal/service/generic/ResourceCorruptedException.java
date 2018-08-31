package ru.vldf.sportsportal.service.generic;

public class ResourceCorruptedException extends Exception {

    public ResourceCorruptedException(String message) {
        super(message);
    }

    public ResourceCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
