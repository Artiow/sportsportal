package ru.vldf.sportsportal.dto.sectional.lease.shortcut;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class OrderShortDTO extends AbstractIdentifiedDTO {

    private Integer id;
    private Boolean paid;
    private BigDecimal price;
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

    public URI getPlaygroundURL() {
        return playgroundURL;
    }

    public OrderShortDTO setPlaygroundURL(URI playgroundURL) {
        this.playgroundURL = playgroundURL;
        return this;
    }

    public URI getCustomerURL() {
        return customerURL;
    }

    public OrderShortDTO setCustomerURL(URI customerURL) {
        this.customerURL = customerURL;
        return this;
    }

    public List<OrderPositionDTO> getPositions() {
        return positions;
    }

    public OrderShortDTO setPositions(List<OrderPositionDTO> positions) {
        this.positions = positions;
        return this;
    }


    public static class OrderPositionDTO implements DataTransferObject {

        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private BigDecimal price;

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

        public BigDecimal getPrice() {
            return price;
        }

        public OrderPositionDTO setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }
    }
}
