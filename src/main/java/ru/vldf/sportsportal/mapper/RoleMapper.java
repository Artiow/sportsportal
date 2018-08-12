package ru.vldf.sportsportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.dto.RoleDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractMapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends AbstractMapper<RoleEntity, RoleDTO> {

    @Mapping(target = "id", ignore = true)
    RoleEntity toEntity(RoleDTO dto);

    default String toString(RoleEntity entity) {
        return entity.getCode();
    }

    default String toString(RoleDTO dto) {
        return dto.getCode();
    }
}
