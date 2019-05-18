package ru.vldf.sportsportal.mapper.sectional.booking;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.booking.FeatureEntity;
import ru.vldf.sportsportal.dto.sectional.booking.FeatureDTO;
import ru.vldf.sportsportal.mapper.general.AbstractDictionaryMapper;

/**
 * @author Namednev Artem
 */
@Mapper
public abstract class FeatureMapper extends AbstractDictionaryMapper<FeatureEntity, FeatureDTO> {

}
