package ru.vldf.sportsportal.domain.sectional.lease;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class ReservationEntityPK implements Serializable {

    @ManyToOne
    private OrderEntity order;

    @ManyToOne
    private PlaygroundEntity playground;


    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public PlaygroundEntity getPlayground() {
        return playground;
    }

    public void setPlayground(PlaygroundEntity playground) {
        this.playground = playground;
    }
}
