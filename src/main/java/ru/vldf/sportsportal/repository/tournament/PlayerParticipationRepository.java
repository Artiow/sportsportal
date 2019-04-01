package ru.vldf.sportsportal.repository.tournament;

import ru.vldf.sportsportal.domain.sectional.tournament.PlayerParticipationEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.PlayerParticipationEntityPK;
import ru.vldf.sportsportal.repository.JpaExecutableRepository;

/**
 * @author Namednev Artem
 */
public interface PlayerParticipationRepository extends JpaExecutableRepository<PlayerParticipationEntity, PlayerParticipationEntityPK> {

}
