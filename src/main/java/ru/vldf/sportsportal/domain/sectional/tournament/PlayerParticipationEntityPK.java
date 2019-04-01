package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class PlayerParticipationEntityPK implements Serializable {

    private static final long serialVersionUID = 1L;


    @ManyToOne(fetch = FetchType.LAZY)
    private TournamentEntity tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlayerEntity player;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamEntity team;
}
