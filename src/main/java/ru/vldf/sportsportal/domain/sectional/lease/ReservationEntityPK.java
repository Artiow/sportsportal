package ru.vldf.sportsportal.domain.sectional.lease;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Embeddable
public class ReservationEntityPK implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaygroundEntity playground;

    @Basic
    @Column(name = "datetime")
    private Timestamp datetime;


    public PlaygroundEntity getPlayground() {
        return playground;
    }

    public void setPlayground(PlaygroundEntity playground) {
        this.playground = playground;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp reservedDate) {
        this.datetime = reservedDate;
    }


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
