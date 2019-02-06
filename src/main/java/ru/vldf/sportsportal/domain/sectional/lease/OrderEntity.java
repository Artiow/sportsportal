package ru.vldf.sportsportal.domain.sectional.lease;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "order", schema = "lease")
public class OrderEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "sum", nullable = false)
    private BigDecimal sum = BigDecimal.valueOf(0, 2);

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private UserEntity customer;

    @OrderBy("pk.datetime")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<ReservationEntity> reservations;


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
