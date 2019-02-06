package ru.vldf.sportsportal.domain.sectional.common;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "picture", schema = "common")
public class PictureEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "size", nullable = false)
    private Long size;

    @Basic
    @Column(name = "uploaded", nullable = false)
    private Timestamp uploaded;


    @OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY)
    private UserEntity users;

    @ManyToMany(mappedBy = "photos")
    private Collection<PlaygroundEntity> playgrounds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PictureEntity)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
