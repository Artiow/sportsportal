package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;
import ru.vldf.sportsportal.dto.sectional.lease.links.PlaygroundLinkDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class ReservationResumeDTO implements DataTransferObject {

    private PlaygroundLinkDTO playground;
    private List<Item> reservations;
    private BigDecimal totalPrice;


    @Getter
    @Setter
    public static class Item implements DataTransferObject {

        private LocalDateTime datetime;
        private BigDecimal price;
    }
}
