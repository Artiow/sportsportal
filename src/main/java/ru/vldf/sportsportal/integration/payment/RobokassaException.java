package ru.vldf.sportsportal.integration.payment;

/**
 * @author Namednev Artem
 */
public abstract class RobokassaException extends RuntimeException {

    public RobokassaException(String message) {
        super(message);
    }
}
