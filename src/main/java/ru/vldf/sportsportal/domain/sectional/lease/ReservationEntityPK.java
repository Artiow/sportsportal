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
}
