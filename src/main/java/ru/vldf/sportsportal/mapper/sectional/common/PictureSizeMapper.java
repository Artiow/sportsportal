package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.common.PictureSizeEntity;
import ru.vldf.sportsportal.dto.sectional.common.PictureSizeDTO;
import ru.vldf.sportsportal.mapper.general.AbstractDictionaryMapper;
import ru.vldf.sportsportal.service.filesystem.model.PictureSize;

/**
 * @author Namednev Artem
 */
@Mapper
public abstract class PictureSizeMapper extends AbstractDictionaryMapper<PictureSizeEntity, PictureSizeDTO> {

    public PictureSize toSize(PictureSizeEntity entity) {
        if (entity != null) {
            return PictureSize.of(entity.getCode(), entity.getWidth(), entity.getHeight());
        } else {
            return null;
        }
    }

    public PictureSizeEntity merge(PictureSizeEntity acceptor, PictureSizeEntity donor) {
        super.merge(acceptor, donor);
        acceptor.setWidth(donor.getWidth());
        acceptor.setHeight(donor.getHeight());
        acceptor.setIsDefault(donor.getIsDefault());
        return acceptor;
    }
}
