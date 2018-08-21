package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.dto.sectional.common.PictureDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractIdentifiedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;

@Mapper(uses = {JavaTimeMapper.class, PictureURLMapper.class}, componentModel = "spring")
public interface PictureMapper extends AbstractIdentifiedMapper<PictureEntity, PictureDTO> {

    @Mapping(target = "url", source = "id", qualifiedByName = {"toPictureURL", "fromId"})
    PictureDTO toDTO(PictureEntity entity);
}
