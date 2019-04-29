package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.AbstractIdentifiedEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "game", schema = "tournament")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class GameEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "datetime")
    private Timestamp datetime;

    @Basic
    @Column(name = "completed", nullable = false)
    private Boolean isCompleted = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false)
    private TourEntity tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", referencedColumnName = "id", nullable = false)
    private TournamentEntity tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "red_team_id", referencedColumnName = "id", nullable = false)
    private TeamEntity redTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blue_team_id", referencedColumnName = "id", nullable = false)
    private TeamEntity blueTeam;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumns({
            @JoinColumn(name = "red_team_id", referencedColumnName = "team_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id", nullable = false, insertable = false, updatable = false)
    })
    private TeamParticipationEntity redTeamParticipation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumns({
            @JoinColumn(name = "blue_team_id", referencedColumnName = "team_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id", nullable = false, insertable = false, updatable = false)
    })
    private TeamParticipationEntity blueTeamParticipation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_game_id", referencedColumnName = "id")
    private GameEntity childGame;

    @OneToMany(mappedBy = "childGame")
    private Collection<GameEntity> parentGames;

    @OneToMany(mappedBy = "pk.team")
    private Collection<PlayerResultEntity> playerResults;


    @PrePersist
    void prePersist() {
        synchronize();
    }

    @PreUpdate
    void preUpdate() {
        synchronize();
    }

    void synchronize() {
        TournamentEntity redVarTournament = redTeamParticipation.getTournament();
        TournamentEntity blueVarTournament = blueTeamParticipation.getTournament();
        if (!Objects.equals(redVarTournament, blueVarTournament)) {
            throw new IllegalStateException("Tournament entity is ambiguous!");
        } else {
            setRedTeam(redTeamParticipation.getTeam());
            setBlueTeam(blueTeamParticipation.getTeam());
            setTournament(redVarTournament);
        }
    }
}
