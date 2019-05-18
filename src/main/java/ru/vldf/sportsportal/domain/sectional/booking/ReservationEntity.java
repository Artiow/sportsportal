package ru.vldf.sportsportal.domain.sectional.booking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.root.DomainObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "reservation", schema = "booking")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AttributeOverrides({@AttributeOverride(name = "pk.datetime", column = @Column(name = "datetime", nullable = false))})
@AssociationOverrides({@AssociationOverride(name = "pk.playground", joinColumns = @JoinColumn(name = "playground_id", nullable = false))})
public class ReservationEntity implements DomainObject {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private ReservationEntityPK pk;

    @Basic
    @Column(name = "price", nullable = false)
    private BigDecimal price = BigDecimal.valueOf(0, 2);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private OrderEntity order;


    public ReservationEntity() {
        pk = new ReservationEntityPK();
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
}
