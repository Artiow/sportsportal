package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.dto.sectional.common.PictureDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractIdentifiedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;

/**
 * @author Namednev Artem
 */
@Mapper(uses = {JavaTimeMapper.class, PictureURLMapper.class}, componentModel = "spring")
public abstract class PictureMapper extends AbstractIdentifiedMapper<PictureEntity, PictureDTO> {

    @Mapping(target = "url", source = "id", qualifiedByName = {"toPictureURL", "fromId"})
    public abstract PictureDTO toDTO(PictureEntity entity);
}
