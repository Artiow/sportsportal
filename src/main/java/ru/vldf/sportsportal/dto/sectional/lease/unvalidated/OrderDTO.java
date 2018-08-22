package ru.vldf.sportsportal.dto.sectional.lease.unvalidated;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class OrderDTO extends AbstractIdentifiedDTO {

    private Integer id;
    private Integer cost;
    private Boolean paid;
    private LocalDateTime datetime;
    private LocalDateTime expiration;
    private URI playgroundURL;
    private URI customerURL;
    private List<OrderPositionDTO> positions;


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

    public URI getPlaygroundURL() {
        return playgroundURL;
    }

    public OrderDTO setPlaygroundURL(URI playgroundURL) {
        this.playgroundURL = playgroundURL;
        return this;
    }

    public URI getCustomerURL() {
        return customerURL;
    }

    public OrderDTO setCustomerURL(URI customerURL) {
        this.customerURL = customerURL;
        return this;
    }

    public List<OrderPositionDTO> getPositions() {
        return positions;
    }

    public OrderDTO setPositions(List<OrderPositionDTO> positions) {
        this.positions = positions;
        return this;
    }


    public static class OrderPositionDTO implements DataTransferObject {

        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer cost;

        public LocalDate getDate() {
            return date;
        }

        public OrderPositionDTO setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public OrderPositionDTO setStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public OrderPositionDTO setEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Integer getCost() {
            return cost;
        }

        public OrderPositionDTO setCost(Integer cost) {
            this.cost = cost;
            return this;
        }
    }
}
