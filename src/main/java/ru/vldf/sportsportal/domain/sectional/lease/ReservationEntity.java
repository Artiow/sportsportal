package ru.vldf.sportsportal.domain.sectional.lease;

import ru.vldf.sportsportal.domain.generic.DomainObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reservation", schema = "lease")
@AssociationOverrides({
        @AssociationOverride(name = "pk.order", joinColumns = @JoinColumn(name = "order_id"))
})
@AttributeOverrides({
        @AttributeOverride(name = "pk.reservedDate", column = @Column(name = "reserved_date")),
        @AttributeOverride(name = "pk.reservedTime", column = @Column(name = "reserved_time"))
})
public class ReservationEntity implements DomainObject {

    @EmbeddedId
    private ReservationEntityPK pk;

    @Basic
    @Column(name = "cost")
    private Integer cost;


    public ReservationEntity() {
        pk = new ReservationEntityPK();
    }


    public ReservationEntityPK getPk() {
        return pk;
    }

    public void setPk(ReservationEntityPK pk) {
        this.pk = pk;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Transient
    public OrderEntity getOrder() {
        return pk.getOrder();
    }

    public void setOrder(OrderEntity order) {
        pk.setOrder(order);
    }

    @Transient
    public Timestamp getReservedDate() {
        return pk.getReservedDate();
    }

    public void setReservedDate(Timestamp reservedDate) {
        pk.setReservedDate(reservedDate);
    }

    @Transient
    public Timestamp getReservedTime() {
        return pk.getReservedTime();
    }

    public void setReservedTime(Timestamp reservedTime) {
        pk.setReservedTime(reservedTime);
    }
}
