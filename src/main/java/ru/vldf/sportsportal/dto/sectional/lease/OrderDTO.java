package ru.vldf.sportsportal.dto.sectional.lease;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;
import ru.vldf.sportsportal.dto.sectional.common.specialized.UserLinkDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.PlaygroundLinkDTO;

import java.time.LocalDateTime;
import java.util.List;

// todo: add validation!
public class OrderDTO extends AbstractIdentifiedDTO {

    private Integer id;
    private Integer cost;
    private Boolean paid;
    private LocalDateTime datetime;
    private LocalDateTime expiration;
    private UserLinkDTO customer;
    private PlaygroundLinkDTO playground;
    private List<ReservationDTO> reservations;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public OrderDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCost() {
        return cost;
    }

    public OrderDTO setCost(Integer cost) {
        this.cost = cost;
        return this;
    }

    public Boolean getPaid() {
        return paid;
    }

    public OrderDTO setPaid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public OrderDTO setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public OrderDTO setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
        return this;
    }

    public UserLinkDTO getCustomer() {
        return customer;
    }

    public OrderDTO setCustomer(UserLinkDTO customer) {
        this.customer = customer;
        return this;
    }

    public PlaygroundLinkDTO getPlayground() {
        return playground;
    }

    public OrderDTO setPlayground(PlaygroundLinkDTO playground) {
        this.playground = playground;
        return this;
    }

    public List<ReservationDTO> getReservations() {
        return reservations;
    }

    public OrderDTO setReservations(List<ReservationDTO> reservations) {
        this.reservations = reservations;
        return this;
    }
}
