package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.dto.sectional.common.links.PictureLinkDTO;
import ru.vldf.sportsportal.mapper.general.LinkMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(uses = {PictureURLMapper.class})
public abstract class PictureLinkMapper implements LinkMapper<PictureEntity, PictureLinkDTO> {

    @Mapping(target = "pictureURL", source = "id", qualifiedByName = {"toPictureURL", "fromId"})
    public abstract PictureLinkDTO toLinkDTO(PictureEntity entity);
}
