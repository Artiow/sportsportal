package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.AbstractIdentifiedEntity;

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

    @Basic
    @Column(name = "fixed", nullable = false)
    private Boolean isFixed = false;


    @OneToOne(mappedBy = "tournament", cascade = CascadeType.ALL)
    private TourBundleEntity bundle;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Collection<GameEntity> games;

    @OneToMany(mappedBy = "pk.team", cascade = CascadeType.ALL)
    private Collection<TeamParticipationEntity> teamParticipations;

    @OneToMany(mappedBy = "pk.tournament", cascade = CascadeType.ALL)
    private Collection<PlayerParticipationEntity> playerParticipations;

    @OneToMany(mappedBy = "pk.team", cascade = CascadeType.ALL)
    private Collection<PlayerResultEntity> playerResults;
}
