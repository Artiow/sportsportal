package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamParticipationEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TeamLinkDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.specialized.TeamParticipationDTO;
import ru.vldf.sportsportal.mapper.general.BasicMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(uses = {TournamentMapper.class, TeamMapper.class, PlayerParticipationMapper.class})
public abstract class TeamParticipationMapper implements BasicMapper<TeamParticipationEntity, TeamParticipationDTO> {

    @Autowired
    private TeamMapper teamMapper;


    @Mappings({
            @Mapping(target = "name", source = "participationName"),
            @Mapping(target = "players", source = "playerParticipations")
    })
    public abstract TeamParticipationDTO toDTO(TeamParticipationEntity entity);

    @Mappings({
            @Mapping(target = "participationName", ignore = true),
            @Mapping(target = "playerParticipations", ignore = true),
            @Mapping(target = "isFixed", nullValuePropertyMappingStrategy = IGNORE)
    })
    public abstract TeamParticipationEntity toEntity(TeamParticipationDTO dto);


    public TeamDTO toTeamDTO(TeamParticipationEntity entity) {
        return teamMapper.toDTO(entity.getTeam());
    }

    public List<TeamDTO> toTeamDTO(Collection<TeamParticipationEntity> entityCollection) {
        return Optional.ofNullable(entityCollection).map(c -> c.stream().map(this::toTeamDTO).collect(Collectors.toList())).orElse(null);
    }

    public TeamLinkDTO toTeamLinkDTO(TeamParticipationEntity entity) {
        return teamMapper.toLinkDTO(entity.getTeam());
    }

    public List<TeamLinkDTO> toTeamLinkDTO(Collection<TeamParticipationEntity> entityCollection) {
        return Optional.ofNullable(entityCollection).map(c -> c.stream().map(this::toTeamLinkDTO).collect(Collectors.toList())).orElse(null);
    }
}
