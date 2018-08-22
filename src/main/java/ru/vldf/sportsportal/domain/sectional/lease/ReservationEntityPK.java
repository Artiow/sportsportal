package ru.vldf.sportsportal.domain.sectional.lease;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Timestamp;

@Embeddable
public class ReservationEntityPK implements Serializable {

    @ManyToOne
    private OrderEntity order;

    @Basic
    @Column(name = "reserved_date")
    private Timestamp reservedDate;

    @Basic
    @Column(name = "reserved_time")
    private Timestamp reservedTime;


    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Timestamp getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(Timestamp reservedDate) {
        this.reservedDate = reservedDate;
    }

    public Timestamp getReservedTime() {
        return reservedTime;
    }

    public void setReservedTime(Timestamp reservedTime) {
        this.reservedTime = reservedTime;
    }
}
