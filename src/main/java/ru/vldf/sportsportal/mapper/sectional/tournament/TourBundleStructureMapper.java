package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.tournament.TourBundleStructureEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TourBundleStructureDTO;
import ru.vldf.sportsportal.mapper.general.AbstractDictionaryMapper;

/**
 * @author Namednev Artem
 */
@Mapper(componentModel = "spring")
public abstract class TourBundleStructureMapper extends AbstractDictionaryMapper<TourBundleStructureEntity, TourBundleStructureDTO> {

}
