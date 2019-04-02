package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.DomainObject;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "team_participation", schema = "tournament", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tournament_id", "participation_name"})
})
@AssociationOverrides({
        @AssociationOverride(name = "pk.team", joinColumns = @JoinColumn(name = "team_id", nullable = false)),
        @AssociationOverride(name = "pk.tournament", joinColumns = @JoinColumn(name = "tournament_id", nullable = false))
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TeamParticipationEntity implements DomainObject {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private TeamParticipationEntityPK pk;

    @Basic
    @Column(name = "participation_name", nullable = false)
    private String participationName;


    @OneToMany(mappedBy = "teamParticipation")
    private Collection<PlayerParticipationEntity> playerParticipations;

    @OneToMany(mappedBy = "redTeamParticipation")
    private Collection<GameEntity> likeRedGames;

    @OneToMany(mappedBy = "blueTeamParticipation")
    private Collection<GameEntity> likeBlueGames;


    public TeamParticipationEntity() {
        pk = new TeamParticipationEntityPK();
    }


    @Transient
    public TeamEntity getTeam() {
        return pk.getTeam();
    }

    public void setTeam(TeamEntity team) {
        pk.setTeam(team);
    }

    @Transient
    public TournamentEntity getTournament() {
        return pk.getTournament();
    }

    public void setTournament(TournamentEntity tournament) {
        pk.setTournament(tournament);
    }
}
