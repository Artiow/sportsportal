package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.root.DomainObject;

import javax.persistence.*;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "player_result", schema = "tournament", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"game_id", "player_id"})
})
@AssociationOverrides({
        @AssociationOverride(name = "pk.game", joinColumns = @JoinColumn(name = "game_id", nullable = false)),
        @AssociationOverride(name = "pk.tournament", joinColumns = @JoinColumn(name = "tournament_id", nullable = false)),
        @AssociationOverride(name = "pk.player", joinColumns = @JoinColumn(name = "player_id", nullable = false)),
        @AssociationOverride(name = "pk.team", joinColumns = @JoinColumn(name = "team_id", nullable = false))
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlayerResultEntity implements DomainObject {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private PlayerResultEntityPK pk;

    @Basic
    @Column(name = "attended", nullable = false)
    private Boolean isAttended = true;

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
            @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "player_id", referencedColumnName = "player_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false, insertable = false, updatable = false)
    })
    private PlayerParticipationEntity playerParticipation;


    public PlayerResultEntity() {
        pk = new PlayerResultEntityPK();
    }


    @Transient
    public GameEntity getGame() {
        return pk.getGame();
    }

    public void setGame(GameEntity game) {
        pk.setGame(game);
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
        setTournament(playerParticipation.getTournament());
        setPlayer(playerParticipation.getPlayer());
        setTeam(playerParticipation.getTeam());
    }
}
