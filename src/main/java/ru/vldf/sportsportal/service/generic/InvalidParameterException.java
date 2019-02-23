package ru.vldf.sportsportal.service.generic;

/**
 * @author Namednev Artem
 */
public class InvalidParameterException extends Exception {

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
