package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.AbstractRightsBasedEntity;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "player", schema = "tournament", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "surname", "patronymic", "birthdate"})})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class PlayerEntity extends AbstractRightsBasedEntity {

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false)
    private String surname;

    @Basic
    @Column(name = "patronymic")
    private String patronymic;

    @Basic
    @Column(name = "birthdate", nullable = false)
    private Timestamp birthdate;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private PictureEntity avatar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            schema = "tournament",
            name = "player_binding",
            joinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    )
    private UserEntity user;

    @OneToMany(mappedBy = "pk.player")
    private Collection<PlayerParticipationEntity> playerParticipations;

    @OneToMany(mappedBy = "pk.team")
    private Collection<PlayerResultEntity> playerResults;
}
