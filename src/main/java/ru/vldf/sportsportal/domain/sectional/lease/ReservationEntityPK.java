package ru.vldf.sportsportal.domain.sectional.lease;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class ReservationEntityPK implements Serializable {

    @Basic
    @Column(name = "datetime")
    private Timestamp datetime;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaygroundEntity playground;
}
