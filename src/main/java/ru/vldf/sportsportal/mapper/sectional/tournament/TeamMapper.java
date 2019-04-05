package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.*;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class TeamMapper extends AbstractVersionedMapper<TeamEntity, TeamDTO> {

    @Override
    @Mappings({
            @Mapping(target = "isLocked", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
            @Mapping(target = "isDisabled", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
    })
    public abstract TeamEntity toEntity(TeamDTO dto);

    @AfterMapping
    protected void normalize(@MappingTarget TeamEntity entity) {
        if ((entity.getMainCaptain() != null) || (entity.getViceCaptain() != null)) {
            if (entity.getMainCaptain() == null) {
                entity.setMainCaptain(entity.getMainCaptain());
            }
            if (entity.getViceCaptain() == null) {
                entity.setViceCaptain(entity.getViceCaptain());
            }
        }
    }
}
