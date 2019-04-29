package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.tournament.TourBundleEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TourBundleDTO;
import ru.vldf.sportsportal.mapper.general.AbstractIdentifiedMapper;

/**
 * @author Artem Namednev
 */
@Mapper
public abstract class TourBundleMapper extends AbstractIdentifiedMapper<TourBundleEntity, TourBundleDTO> {

}
