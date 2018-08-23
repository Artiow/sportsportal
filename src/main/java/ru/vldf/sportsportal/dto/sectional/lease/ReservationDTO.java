package ru.vldf.sportsportal.dto.sectional.lease;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReservationDTO implements DataTransferObject {

    @NotNull
    // todo: future datetime check!
    private LocalDateTime datetime;

    @NotNull
    @Min(value = 0)
    private Integer cost;


    public LocalDateTime getDatetime() {
        return datetime;
    }

    public ReservationDTO setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    public Integer getCost() {
        return cost;
    }

    public ReservationDTO setCost(Integer cost) {
        this.cost = cost;
        return this;
    }
}
