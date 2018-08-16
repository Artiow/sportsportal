package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.lease.FeatureEntity;
import ru.vldf.sportsportal.dto.sectional.lease.FeatureDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractWordbookMapper;

@Mapper(componentModel = "spring")
public interface FeatureMapper extends AbstractWordbookMapper<FeatureEntity, FeatureDTO> {

}
