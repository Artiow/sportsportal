package ru.vldf.sportsportal.domain.sectional.common;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "picture", schema = "common")
public class PictureEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "upload_date", nullable = false)
    private Timestamp uploaded;


    @OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY)
    private UserEntity users;

    @ManyToMany(mappedBy = "photos", fetch = FetchType.LAZY)
    private Collection<PlaygroundEntity> playgrounds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", referencedColumnName = "id")
    private UserEntity uploader;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PictureEntity)) return false;
        return super.equals(o);
    }
}
