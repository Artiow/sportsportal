package ru.vldf.sportsportal.domain.sectional.lease;

import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "order", schema = "lease")
public class OrderEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "price", nullable = false)
    private BigDecimal price = BigDecimal.valueOf(0, 2);

    @Basic
    @Column(name = "paid", nullable = false)
    private Boolean paid = false;

    @Basic
    @Column(name = "datetime", nullable = false)
    private Timestamp datetime;

    @Basic
    @Column(name = "expiration")
    private Timestamp expiration;

    @Basic
    @Column(name = "by_owner")
    private Boolean byOwner = false;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private UserEntity customer;

    @OrderBy("pk.datetime")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<ReservationEntity> reservations;


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public Boolean getByOwner() {
        return byOwner;
    }

    public void setByOwner(Boolean byOwner) {
        this.byOwner = byOwner;
    }

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public Collection<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<ReservationEntity> reservations) {
        this.reservations = reservations;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEntity)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
