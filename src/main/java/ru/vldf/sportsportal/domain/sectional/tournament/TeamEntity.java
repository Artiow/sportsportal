package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.AbstractAuditableEntity;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "team", schema = "tournament")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class TeamEntity extends AbstractAuditableEntity {

    @Basic
    @Column(name = "name", nullable = false, unique = true)
    private String name;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private PictureEntity avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_captain_id", referencedColumnName = "id", nullable = false)
    private UserEntity mainCaptain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vice_captain_id", referencedColumnName = "id", nullable = false)
    private UserEntity viceCaptain;

    @OneToMany(mappedBy = "redTeam")
    private Collection<GameEntity> likeRedGames;

    @OneToMany(mappedBy = "blueTeam")
    private Collection<GameEntity> likeBlueGames;

    @OneToMany(mappedBy = "pk.team")
    private Collection<TeamParticipationEntity> teamParticipations;

    @OneToMany(mappedBy = "pk.team")
    private Collection<PlayerParticipationEntity> playerParticipations;

    @OneToMany(mappedBy = "pk.team")
    private Collection<PlayerResultEntity> playerResults;
}
