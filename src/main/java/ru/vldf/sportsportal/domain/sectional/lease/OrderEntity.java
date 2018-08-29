package ru.vldf.sportsportal.domain.sectional.lease;

import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "order", schema = "lease")
public class OrderEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "cost", nullable = false)
    private Integer cost = 0;

    @Basic
    @Column(name = "paid", nullable = false)
    private Boolean paid = false;

    @Basic
    @Column(name = "datetime", nullable = false)
    private Timestamp datetime;

    @Basic
    @Column(name = "expiration")
    private Timestamp expiration;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private UserEntity customer;

    @ManyToOne
    @JoinColumn(name = "playground_id", referencedColumnName = "id")
    private PlaygroundEntity playground;

    @OrderBy("pk.datetime")
    @OneToMany(mappedBy = "pk.order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<ReservationEntity> reservations;


    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
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

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public PlaygroundEntity getPlayground() {
        return playground;
    }

    public void setPlayground(PlaygroundEntity playground) {
        this.playground = playground;
    }

    public Collection<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
}
