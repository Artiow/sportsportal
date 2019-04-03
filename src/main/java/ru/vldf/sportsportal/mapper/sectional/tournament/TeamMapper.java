package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

/**
 * @author Namednev Artem
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class TeamMapper extends AbstractVersionedMapper<TeamEntity, TeamDTO> {

}
