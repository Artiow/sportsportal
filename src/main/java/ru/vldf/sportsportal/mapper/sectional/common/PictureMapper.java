package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.dto.sectional.common.PictureDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractIdentifiedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.uri.common.PictureURIMapper;

@Mapper(uses = {JavaTimeMapper.class, PictureURIMapper.class}, componentModel = "spring")
public interface PictureMapper extends AbstractIdentifiedMapper<PictureEntity, PictureDTO> {

    @Mapping(target = "uri", source = "id", qualifiedByName = {"PictureURIMapper", "toPictureURI"})
    PictureDTO toDTO(PictureEntity entity);
}
