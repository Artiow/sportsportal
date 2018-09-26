package ru.vldf.sportsportal.dto.sectional.lease;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;
import ru.vldf.sportsportal.dto.validation.annotations.Future;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationDTO implements DataTransferObject {

    @NotNull
    @Future
    private LocalDateTime datetime;

    @NotNull
    @Min(value = 0)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;


    public LocalDateTime getDatetime() {
        return datetime;
    }

    public ReservationDTO setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ReservationDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
