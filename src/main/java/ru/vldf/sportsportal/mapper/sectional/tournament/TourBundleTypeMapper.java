package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.tournament.TourBundleTypeEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TourBundleTypeDTO;
import ru.vldf.sportsportal.mapper.general.AbstractDictionaryMapper;

/**
 * @author Namednev Artem
 */
@Mapper(componentModel = "spring")
public abstract class TourBundleTypeMapper extends AbstractDictionaryMapper<TourBundleTypeEntity, TourBundleTypeDTO> {

}
