package ru.vldf.sportsportal.domain.sectional.lease;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Embeddable
public class ReservationEntityPK implements Serializable {

    @Basic
    @Column(name = "datetime")
    private Timestamp datetime;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaygroundEntity playground;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationEntityPK)) return false;
        ReservationEntityPK that = (ReservationEntityPK) o;
        return Objects.equals(getPlayground(), that.getPlayground()) &&
                Objects.equals(getDatetime(), that.getDatetime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayground(), getDatetime());
    }
}
