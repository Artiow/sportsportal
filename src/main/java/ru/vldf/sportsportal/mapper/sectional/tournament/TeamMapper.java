package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.*;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;

/**
 * @author Namednev Artem
 */
@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, PictureURLMapper.class, PictureMapper.class}
)
@SuppressWarnings("UnmappedTargetProperties")
public abstract class TeamMapper extends AbstractVersionedMapper<TeamEntity, TeamDTO> {

    @Override
    @Mappings({
            @Mapping(target = "isLocked", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
            @Mapping(target = "isDisabled", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    })
    public abstract TeamEntity toEntity(TeamDTO dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "avatar", ignore = true) // todo: remove!
    })
    public abstract void toEntity(@MappingTarget TeamEntity acceptor, TeamEntity donor);


    @Override
    public TeamEntity merge(TeamEntity acceptor, TeamEntity donor) throws OptimisticLockException {
        super.merge(acceptor, donor);
        toEntity(acceptor, donor);
        return acceptor;
    }

    @AfterMapping
    public void normalize(@MappingTarget TeamEntity entity) {
        if ((entity.getMainCaptain() != null) || (entity.getViceCaptain() != null)) {
            if (entity.getMainCaptain() == null) {
                entity.setMainCaptain(entity.getViceCaptain());
            }
            if (entity.getViceCaptain() == null) {
                entity.setViceCaptain(entity.getMainCaptain());
            }
        }
    }
}
