package ru.vldf.sportsportal.repository.tournament;

import ru.vldf.sportsportal.domain.sectional.tournament.TeamParticipationEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamParticipationEntityPK;
import ru.vldf.sportsportal.repository.JpaExecutableRepository;

/**
 * @author Namednev Artem
 */
public interface TeamParticipationRepository extends JpaExecutableRepository<TeamParticipationEntity, TeamParticipationEntityPK> {

}
