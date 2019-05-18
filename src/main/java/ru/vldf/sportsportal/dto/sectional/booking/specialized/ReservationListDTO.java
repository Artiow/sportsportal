package ru.vldf.sportsportal.dto.sectional.booking.specialized;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;
import ru.vldf.sportsportal.dto.validation.annotations.Future;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class ReservationListDTO implements DataTransferObject {

    @NotEmpty
    private List<@Future LocalDateTime> reservations;
}
