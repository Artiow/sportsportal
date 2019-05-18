package ru.vldf.sportsportal.dto.sectional.booking.shortcut;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractVersionedShortDTO;
import ru.vldf.sportsportal.dto.sectional.booking.specialized.ReservationResumeDTO;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class OrderShortDTO extends AbstractVersionedShortDTO {

    private BigDecimal price;
    private LocalDateTime datetime;
    private LocalDateTime expiration;
    private Boolean isPaid;
    private Boolean isOwned;
    private Boolean isFreed;
    private URI orderURL;
    private URI customerURL;
    private List<ReservationResumeDTO> reservations;
}
