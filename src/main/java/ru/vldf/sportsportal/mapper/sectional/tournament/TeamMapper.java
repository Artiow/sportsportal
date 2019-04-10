package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.*;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(componentModel = "spring", uses = {UserMapper.class, PictureURLMapper.class, PictureMapper.class})
public abstract class TeamMapper extends AbstractVersionedMapper<TeamEntity, TeamDTO> {

    @Override
    @Mappings({
            @Mapping(target = "isLocked", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
            @Mapping(target = "isDisabled", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    })
    public abstract TeamEntity toEntity(TeamDTO dto);


    @Override
    public TeamEntity merge(TeamEntity acceptor, TeamEntity donor) throws OptimisticLockException {
        super.merge(acceptor, donor);

        acceptor.setName(donor.getName());
        acceptor.setIsLocked(donor.getIsLocked());
        acceptor.setIsDisabled(donor.getIsDisabled());

        if (!Objects.equals(acceptor.getMainCaptain(), donor.getMainCaptain())) {
            acceptor.setMainCaptain(donor.getMainCaptain());
        }

        if (!Objects.equals(acceptor.getViceCaptain(), donor.getViceCaptain())) {
            acceptor.setViceCaptain(donor.getViceCaptain());
        }

        normalize(acceptor);
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
