package ru.vldf.sportsportal.dto.sectional.lease.shortcut;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.ReservationResumeDTO;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

public class OrderShortDTO extends AbstractIdentifiedDTO {

    private Integer id;
    private Boolean paid;
    private BigDecimal price;
    private LocalDateTime datetime;
    private LocalDateTime expiration;
    private URI orderURL;
    private URI customerURL;
    private List<ReservationResumeDTO> reservations;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public OrderShortDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Boolean getPaid() {
        return paid;
    }

    public OrderShortDTO setPaid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderShortDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public OrderShortDTO setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public OrderShortDTO setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
        return this;
    }

    public URI getOrderURL() {
        return orderURL;
    }

    public OrderShortDTO setOrderURL(URI orderURL) {
        this.orderURL = orderURL;
        return this;
    }

    public URI getCustomerURL() {
        return customerURL;
    }

    public OrderShortDTO setCustomerURL(URI customerURL) {
        this.customerURL = customerURL;
        return this;
    }

    public List<ReservationResumeDTO> getReservations() {
        return reservations;
    }

    public OrderShortDTO setReservations(List<ReservationResumeDTO> reservations) {
        this.reservations = reservations;
        return this;
    }
}
