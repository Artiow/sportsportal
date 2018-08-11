package ru.vldf.sportsportal.domain;

import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "picture", schema = "common")
public class PictureEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "size", nullable = false)
    private Long size;

    @Basic
    @Column(name = "uploaded", nullable = false)
    private Timestamp uploaded;


    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Timestamp getUploaded() {
        return uploaded;
    }

    public void setUploaded(Timestamp uploaded) {
        this.uploaded = uploaded;
    }
}
