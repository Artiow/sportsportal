package ru.vldf.sportsportal.integration.payment;

/**
 * @author Namednev Artem
 */
public class RobokassaSecurityException extends RobokassaException {

    private Integer invId;

    public RobokassaSecurityException(String message, Integer invId) {
        super(message);
        this.invId = invId;
    }

    public Integer getInvId() {
        return invId;
    }
}
