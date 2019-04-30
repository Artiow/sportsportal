package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vldf.sportsportal.domain.sectional.tournament.PlayerParticipationEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.PlayerDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.PlayerLinkDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.specialized.PlayerParticipationDTO;
import ru.vldf.sportsportal.mapper.general.BasicMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper/*(uses = {TournamentMapper.class, TeamMapper.class, PlayerMapper.class})*/
public abstract class PlayerParticipationMapper implements BasicMapper<PlayerParticipationEntity, PlayerParticipationDTO> {

    @Autowired
    private PlayerMapper playerMapper;


    public PlayerParticipationDTO toDTO(PlayerParticipationEntity entity) {
        throw new UnsupportedOperationException();
    }

    public PlayerParticipationEntity toEntity(PlayerParticipationDTO dto) {
        throw new UnsupportedOperationException();
    }


    public PlayerDTO toPlayerDTO(PlayerParticipationEntity entity) {
        return playerMapper.toDTO(entity.getPlayer());
    }

    public List<PlayerDTO> toPlayerDTO(Collection<PlayerParticipationEntity> entityCollection) {
        return Optional.ofNullable(entityCollection).map(c -> c.stream().map(this::toPlayerDTO).collect(Collectors.toList())).orElse(null);
    }

    public PlayerLinkDTO toPlayerLinkDTO(PlayerParticipationEntity entity) {
        return playerMapper.toLinkDTO(entity.getPlayer());
    }

    public List<PlayerLinkDTO> toPlayerLinkDTO(Collection<PlayerParticipationEntity> entityCollection) {
        return Optional.ofNullable(entityCollection).map(c -> c.stream().map(this::toPlayerLinkDTO).collect(Collectors.toList())).orElse(null);
    }
}
