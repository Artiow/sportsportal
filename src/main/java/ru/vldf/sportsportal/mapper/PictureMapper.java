package ru.vldf.sportsportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.PictureEntity;
import ru.vldf.sportsportal.dto.PictureDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.uri.PictureURIMapper;

@Mapper(uses = {JavaTimeMapper.class, PictureURIMapper.class}, componentModel = "spring")
public interface PictureMapper extends AbstractMapper<PictureEntity, PictureDTO> {

    @Mapping(target = "uri", source = "id")
    PictureDTO toDTO(PictureEntity entity);
}
