package ru.vldf.sportsportal.service.generic;

public class SentDataCorruptedException extends Exception {

    public SentDataCorruptedException(String message) {
        super(message);
    }

    public SentDataCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
