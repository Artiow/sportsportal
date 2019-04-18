package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.root.EmbeddedObject;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class PlayerParticipationEntityPK implements EmbeddedObject {

    private static final long serialVersionUID = 1L;


    @ManyToOne(fetch = FetchType.LAZY)
    private TournamentEntity tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlayerEntity player;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamEntity team;
}
