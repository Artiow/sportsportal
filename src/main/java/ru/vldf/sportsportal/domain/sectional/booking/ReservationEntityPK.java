package ru.vldf.sportsportal.domain.sectional.booking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.root.EmbeddedObject;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class ReservationEntityPK implements EmbeddedObject {

    private static final long serialVersionUID = 1L;


    @Basic
    private Timestamp datetime;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaygroundEntity playground;
}
