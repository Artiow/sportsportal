package ru.vldf.sportsportal.domain.sectional.lease;

import ru.vldf.sportsportal.domain.generic.DomainObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reservation", schema = "lease")
@AssociationOverrides({
        @AssociationOverride(name = "pk.order", joinColumns = @JoinColumn(name = "order_id")),
        @AssociationOverride(name = "pk.playground", joinColumns = @JoinColumn(name = "playground_id"))
})
public class ReservationEntity implements DomainObject {

    @EmbeddedId
    private ReservationEntityPK pk;

    @Basic
    @Column(name = "datetime", nullable = false)
    private Timestamp datetime;


    public ReservationEntity() {
        pk = new ReservationEntityPK();
    }


    public ReservationEntityPK getPk() {
        return pk;
    }

    public void setPk(ReservationEntityPK pk) {
        this.pk = pk;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    @Transient
    public OrderEntity getOrder() {
        return pk.getOrder();
    }

    public void setOrder(OrderEntity order) {
        pk.setOrder(order);
    }

    @Transient
    public PlaygroundEntity getPlayground() {
        return pk.getPlayground();
    }

    public void setPlayground(PlaygroundEntity playground) {
        pk.setPlayground(playground);
    }
}
