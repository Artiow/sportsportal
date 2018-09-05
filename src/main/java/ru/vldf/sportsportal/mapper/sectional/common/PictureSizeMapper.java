package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.common.PictureSizeEntity;
import ru.vldf.sportsportal.dto.sectional.common.PictureSizeDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractDictionaryMapper;
import ru.vldf.sportsportal.service.PictureService;

@Mapper(componentModel = "spring")
public interface PictureSizeMapper extends AbstractDictionaryMapper<PictureSizeEntity, PictureSizeDTO> {

    default PictureService.PictureSize toSize(PictureSizeEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return new PictureService.PictureSize(entity.getCode(), entity.getWidth(), entity.getHeight());
        }
    }

    default PictureSizeEntity merge(PictureSizeEntity acceptor, PictureSizeEntity donor) {
        AbstractDictionaryMapper.super.merge(acceptor, donor);
        acceptor.setWidth(donor.getWidth());
        acceptor.setHeight(donor.getHeight());
        return acceptor;
    }
}
