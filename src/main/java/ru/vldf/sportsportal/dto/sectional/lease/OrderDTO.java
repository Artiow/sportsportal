package ru.vldf.sportsportal.dto.sectional.lease;

import ru.vldf.sportsportal.dto.generic.AbstractVersionedDTO;
import ru.vldf.sportsportal.dto.sectional.common.specialized.UserLinkDTO;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.ReservationResumeDTO;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO extends AbstractVersionedDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotNull(groups = VersionCheck.class)
    @Min(value = 0, groups = VersionCheck.class)
    private Long version;

    @NotNull(groups = FieldCheck.class)
    @Min(value = 0, groups = FieldCheck.class)
    @Digits(integer = 6, fraction = 2, groups = FieldCheck.class)
    private BigDecimal price;

    @NotNull(groups = FieldCheck.class)
    private Boolean paid;

    @NotNull(groups = FieldCheck.class)
    private LocalDateTime datetime;

    @NotNull(groups = FieldCheck.class)
    private LocalDateTime expiration;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private UserLinkDTO customer;

    @Valid
    @NotEmpty(groups = FieldCheck.class)
    private List<ReservationResumeDTO> reservations;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public OrderDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public OrderDTO setVersion(Long version) {
        this.version = version;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderDTO setPrice(BigDecimal price) {
        this.price = price;
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

    public List<ReservationResumeDTO> getReservations() {
        return reservations;
    }

    public OrderDTO setReservations(List<ReservationResumeDTO> reservations) {
        this.reservations = reservations;
        return this;
    }


    public interface IdCheck extends VersionCheck {

    }

    public interface CreateCheck extends FieldCheck {

    }

    public interface UpdateCheck extends VersionCheck, FieldCheck {

    }

    private interface VersionCheck {

    }

    private interface FieldCheck {

    }
}
