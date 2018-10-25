package ru.vldf.sportsportal.domain.sectional.security;

import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "key", schema = "security")
public class KeyEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Basic
    @Column(name = "type", nullable = false)
    private String type;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "related_id", referencedColumnName = "id")
    private KeyEntity related;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public KeyEntity setType(String type) {
        this.type = type;
        return this;
    }

    public KeyEntity getRelated() {
        return related;
    }

    public KeyEntity setRelated(KeyEntity related) {
        this.related = related;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
