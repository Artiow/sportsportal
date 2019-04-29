package ru.vldf.sportsportal.repository.tournament;

import ru.vldf.sportsportal.domain.sectional.tournament.TourBundleEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TournamentEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

/**
 * @author Namednev Artem
 */
public interface TournamentRepository extends AbstractIdentifiedRepository<TournamentEntity> {

    boolean existsByBundleParentAndBundleTextLabel(TourBundleEntity bundleParent, String bundleTextLabel);
}
