package ru.vldf.sportsportal.domain.sectional.common;

import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "picture", schema = "common")
public class PictureEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "size", nullable = false)
    private Long size;

    @Basic
    @Column(name = "uploaded", nullable = false)
    private Timestamp uploaded;

    @OneToOne(
            mappedBy = "avatar",
            fetch = FetchType.LAZY
    )
    private UserEntity users;

    @ManyToMany(mappedBy = "photos")
    private Collection<PlaygroundEntity> playgrounds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;


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

    public UserEntity getUsers() {
        return users;
    }

    public PictureEntity setUsers(UserEntity users) {
        this.users = users;
        return this;
    }

    public Collection<PlaygroundEntity> getPlaygrounds() {
        return playgrounds;
    }

    public void setPlaygrounds(Collection<PlaygroundEntity> playgrounds) {
        this.playgrounds = playgrounds;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }


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
