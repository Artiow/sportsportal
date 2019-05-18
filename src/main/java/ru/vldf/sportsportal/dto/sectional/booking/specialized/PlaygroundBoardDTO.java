package ru.vldf.sportsportal.dto.sectional.booking.specialized;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;
import ru.vldf.sportsportal.dto.sectional.booking.links.PlaygroundLinkDTO;

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
    private Boolean isTested;
    private Boolean isFreed;
}
