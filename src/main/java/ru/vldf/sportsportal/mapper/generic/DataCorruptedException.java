package ru.vldf.sportsportal.mapper.generic;

public class DataCorruptedException extends RuntimeException {

    public DataCorruptedException(String message) {
        super(message);
    }

    public DataCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
