package ru.vldf.sportsportal.mapper.general.throwable;

/**
 * @author Namednev Artem
 */
public class DataCorruptedException extends RuntimeException {

    public DataCorruptedException(String message) {
        super(message);
    }

    public DataCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
