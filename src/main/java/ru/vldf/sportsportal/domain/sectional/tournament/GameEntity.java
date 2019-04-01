package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;

import javax.persistence.*;
import java.sql.Timestamp;

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
    @JoinColumn(name = "tournament_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TournamentEntity tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_game_id", referencedColumnName = "id")
    private GameEntity childGame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "red_team_id", referencedColumnName = "team_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id", nullable = false, insertable = false, updatable = false)
    })
    private TeamParticipationEntity redTeamParticipation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "blue_team_id", referencedColumnName = "team_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id", nullable = false, insertable = false, updatable = false)
    })
    private TeamParticipationEntity blueTeamParticipation;
}
