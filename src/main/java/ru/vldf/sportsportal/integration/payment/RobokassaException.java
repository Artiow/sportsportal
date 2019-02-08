package ru.vldf.sportsportal.integration.payment;

/**
 * @author Namednev Artem
 */
public class RobokassaException extends RuntimeException {

    public RobokassaException(String message) {
        super(message);
    }
}
