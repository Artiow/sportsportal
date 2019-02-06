package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.common.PictureSizeEntity;
import ru.vldf.sportsportal.dto.sectional.common.PictureSizeDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractDictionaryMapper;
import ru.vldf.sportsportal.service.PictureService;

/**
 * @author Namednev Artem
 */
@Mapper(componentModel = "spring")
public abstract class PictureSizeMapper extends AbstractDictionaryMapper<PictureSizeEntity, PictureSizeDTO> {

    public PictureService.PictureSize toSize(PictureSizeEntity entity) {
        if (entity != null) {
            return new PictureService.PictureSize(entity.getCode(), entity.getWidth(), entity.getHeight());
        } else {
            return null;
        }
    }

    public PictureSizeEntity merge(PictureSizeEntity acceptor, PictureSizeEntity donor) {
        super.merge(acceptor, donor);
        acceptor.setWidth(donor.getWidth());
        acceptor.setHeight(donor.getHeight());
        return acceptor;
    }
}
