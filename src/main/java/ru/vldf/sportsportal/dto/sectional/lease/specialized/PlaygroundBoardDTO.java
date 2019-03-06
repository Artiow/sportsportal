package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlaygroundBoardDTO implements DataTransferObject {

    private PlaygroundLinkDTO playground;
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private ReservationGridDTO grid;
    private String phone;
    private Boolean test;
}
