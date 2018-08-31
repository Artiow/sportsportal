package ru.vldf.sportsportal.mapper.generic;

public class DataCorruptedException extends Exception {

    public DataCorruptedException(String message) {
        super(message);
    }

    public DataCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
