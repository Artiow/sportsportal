package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;
import ru.vldf.sportsportal.dto.sectional.lease.PlaygroundDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.PlaygroundShortDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.manual.uri.common.PictureURIMapper;
import ru.vldf.sportsportal.mapper.manual.uri.lease.PlaygroundURIMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureMapper;

import javax.persistence.OptimisticLockException;

@Mapper(
        componentModel = "spring",
        uses = {PlaygroundURIMapper.class, PictureURIMapper.class, PictureMapper.class, SportMapper.class, FeatureMapper.class}
)
public interface PlaygroundMapper extends AbstractVersionedMapper<PlaygroundEntity, PlaygroundDTO> {

    @Mappings({
            @Mapping(target = "playgroundURI", source = "id", qualifiedByName = {"toPlaygroundURI", "fromId"}),
            @Mapping(target = "photoURIs", source = "photos", qualifiedByName = {"toPictureURI", "fromCollection"})
    })
    PlaygroundShortDTO toShortDTO(PlaygroundEntity entity);

    @Mapping(target = "uri", source = "id", qualifiedByName = {"toPlaygroundURI", "fromId"})
    PlaygroundDTO toDTO(PlaygroundEntity entity);

    @Mapping(target = "id", ignore = true)
    PlaygroundEntity toEntity(PlaygroundDTO dto);

    @Override
    default PlaygroundEntity merge(PlaygroundEntity acceptor, PlaygroundEntity donor) throws OptimisticLockException {
        // todo: merge!
        return acceptor;
    }
}
