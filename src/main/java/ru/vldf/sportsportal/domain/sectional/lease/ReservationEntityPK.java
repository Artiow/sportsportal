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
    @Column(name = "datetime")
    private Timestamp datetime;


    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp reservedDate) {
        this.datetime = reservedDate;
    }
}
