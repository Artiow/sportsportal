package ru.vldf.sportsportal.domain.sectional.lease;

import ru.vldf.sportsportal.domain.generic.DomainObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "reservation", schema = "lease")
@AttributeOverrides({@AttributeOverride(name = "pk.datetime", column = @Column(name = "datetime"))})
@AssociationOverrides({@AssociationOverride(name = "pk.playground", joinColumns = @JoinColumn(name = "playground_id"))})
public class ReservationEntity implements DomainObject {

    @EmbeddedId
    private ReservationEntityPK pk;

    @Basic
    @Column(name = "price")
    private BigDecimal price = BigDecimal.valueOf(0, 2);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderEntity order;


    public ReservationEntity() {
        pk = new ReservationEntityPK();
    }


    public ReservationEntityPK getPk() {
        return pk;
    }

    public void setPk(ReservationEntityPK pk) {
        this.pk = pk;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Transient
    public PlaygroundEntity getPlayground() {
        return pk.getPlayground();
    }

    public void setPlayground(PlaygroundEntity playground) {
        pk.setPlayground(playground);
    }

    @Transient
    public Timestamp getDatetime() {
        return pk.getDatetime();
    }

    public void setDatetime(Timestamp datetime) {
        pk.setDatetime(datetime);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationEntity)) return false;
        ReservationEntity entity = (ReservationEntity) o;
        return Objects.equals(getPk(), entity.getPk());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPk());
    }
}
