package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.DomainObject;

import javax.persistence.*;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "player_participation", schema = "tournament", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"player_id", "tournament_id"})
})
@AssociationOverrides({
        @AssociationOverride(name = "pk.tournament", joinColumns = @JoinColumn(name = "tournament_id", nullable = false)),
        @AssociationOverride(name = "pk.player", joinColumns = @JoinColumn(name = "player_id", nullable = false)),
        @AssociationOverride(name = "pk.team", joinColumns = @JoinColumn(name = "team_id", nullable = false)),
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlayerParticipationEntity implements DomainObject {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private PlayerParticipationEntityPK pk;

    @Basic
    @Column(name = "games_num", nullable = false)
    private Integer gamesNum = 0;

    @Basic
    @Column(name = "goals_num", nullable = false)
    private Integer goalsNum = 0;

    @Basic
    @Column(name = "autogoals_num", nullable = false)
    private Integer autogoalsNum = 0;

    @Basic
    @Column(name = "yellow_cards_num", nullable = false)
    private Integer yellowCardsNum = 0;

    @Basic
    @Column(name = "red_cards_num", nullable = false)
    private Integer redCardsNum = 0;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id", nullable = false, insertable = false, updatable = false)
    })
    private TeamParticipationEntity teamParticipation;


    public PlayerParticipationEntity() {
        pk = new PlayerParticipationEntityPK();
    }


    @Transient
    public TournamentEntity getTournament() {
        return pk.getTournament();
    }

    public void setTournament(TournamentEntity tournament) {
        pk.setTournament(tournament);
    }

    @Transient
    public PlayerEntity getPlayer() {
        return pk.getPlayer();
    }

    public void setPlayer(PlayerEntity player) {
        pk.setPlayer(player);
    }

    @Transient
    public TeamEntity getTeam() {
        return pk.getTeam();
    }

    public void setTeam(TeamEntity team) {
        pk.setTeam(team);
    }


    @PrePersist
    void prePersist() {
        synchronize();
    }

    @PreUpdate
    void preUpdate() {
        synchronize();
    }

    void synchronize() {
        setTournament(teamParticipation.getTournament());
        setTeam(teamParticipation.getTeam());
    }
}
