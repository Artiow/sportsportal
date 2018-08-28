package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;
import ru.vldf.sportsportal.dto.validation.annotations.Future;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationListDTO implements DataTransferObject {

    @NotEmpty
    private List<@Future LocalDateTime> reservations;


    public List<LocalDateTime> getReservations() {
        return reservations;
    }

    public ReservationListDTO setReservations(List<LocalDateTime> reservations) {
        this.reservations = reservations;
        return this;
    }
}
