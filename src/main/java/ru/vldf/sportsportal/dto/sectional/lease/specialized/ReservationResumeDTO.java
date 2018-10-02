package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationResumeDTO implements DataTransferObject {

    private PlaygroundLinkDTO playground;
    private List<ReservationItemDTO> reservations;
    private BigDecimal totalPrice;


    public PlaygroundLinkDTO getPlayground() {
        return playground;
    }

    public ReservationResumeDTO setPlayground(PlaygroundLinkDTO playground) {
        this.playground = playground;
        return this;
    }

    public List<ReservationItemDTO> getReservations() {
        return reservations;
    }

    public ReservationResumeDTO setReservations(List<ReservationItemDTO> reservations) {
        this.reservations = reservations;
        return this;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public ReservationResumeDTO setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }


    public static class ReservationItemDTO implements DataTransferObject {

        private LocalDateTime datetime;
        private BigDecimal price;


        public LocalDateTime getDatetime() {
            return datetime;
        }

        public ReservationItemDTO setDatetime(LocalDateTime datetime) {
            this.datetime = datetime;
            return this;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public ReservationItemDTO setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }
    }
}
