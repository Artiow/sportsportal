package ru.vldf.sportsportal.dto.sectional.lease.shortcut;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.VersionedDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.ReservationResumeDTO;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class OrderShortDTO implements VersionedDTO {

    private Integer id;
    private Long version;
    private Boolean paid;
    private BigDecimal price;
    private LocalDateTime datetime;
    private LocalDateTime expiration;
    private Boolean byOwner;
    private URI orderURL;
    private URI customerURL;
    private List<ReservationResumeDTO> reservations;
}
