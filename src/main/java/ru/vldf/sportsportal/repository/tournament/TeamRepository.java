package ru.vldf.sportsportal.repository.tournament;

import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

/**
 * @author Namednev Artem
 */
public interface TeamRepository extends AbstractIdentifiedRepository<TeamEntity> {

    boolean existsByNameAndIdNot(String name, Integer id);

    boolean existsByName(String name);
}
