package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.lease.FeatureEntity;
import ru.vldf.sportsportal.dto.sectional.lease.FeatureDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractDictionaryMapper;

/**
 * @author Namednev Artem
 */
@Mapper(componentModel = "spring")
public abstract class FeatureMapper extends AbstractDictionaryMapper<FeatureEntity, FeatureDTO> {

}
