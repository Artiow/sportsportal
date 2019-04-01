package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "tournament", schema = "tournament")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class TournamentEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "start_date")
    private Timestamp startDate;

    @Basic
    @Column(name = "finish_date")
    private Timestamp finishDate;

    @Basic
    @Column(name = "completed", nullable = false)
    private Boolean isCompleted = false;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_id", referencedColumnName = "id", nullable = false)
    private TourBundleEntity bundle;

    @OneToMany(mappedBy = "tournament")
    private Collection<GameEntity> games;

    @OneToMany(mappedBy = "pk.team")
    private Collection<TeamParticipationEntity> teamParticipations;

    @OneToMany(mappedBy = "pk.tournament")
    private Collection<PlayerParticipationEntity> playerParticipations;
}
