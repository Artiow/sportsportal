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

    @OneToOne(mappedBy = "avatar")
    private UserEntity users;

    @ManyToMany(mappedBy = "photos")
    private Collection<PlaygroundEntity> playgrounds;


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
}
