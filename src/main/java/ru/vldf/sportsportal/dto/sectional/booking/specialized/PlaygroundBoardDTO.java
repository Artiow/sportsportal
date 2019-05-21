package ru.vldf.sportsportal.dto.sectional.booking.specialized;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;
import ru.vldf.sportsportal.dto.sectional.booking.shortcut.PlaygroundShortDTO;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlaygroundBoardDTO implements DataTransferObject {

    private PlaygroundShortDTO playground;
    private ReservationGridDTO grid;
}
