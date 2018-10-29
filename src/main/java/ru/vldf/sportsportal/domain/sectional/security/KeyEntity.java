package ru.vldf.sportsportal.domain.sectional.security;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "key", schema = "security")
public class KeyEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Basic
    @Column(name = "type", nullable = false)
    private String type;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "iat", nullable = false)
    private Timestamp issuedAt;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "exp", nullable = false)
    private Timestamp expiredAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "related_id", referencedColumnName = "id")
    private KeyEntity related;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
