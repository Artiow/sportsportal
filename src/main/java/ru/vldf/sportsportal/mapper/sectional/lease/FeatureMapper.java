package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.dto.sectional.common.RoleDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractWordbookMapper;

@Mapper(componentModel = "spring")
public interface FeatureMapper extends AbstractWordbookMapper<RoleEntity, RoleDTO> {

}
