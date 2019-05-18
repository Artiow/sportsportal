package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.vldf.sportsportal.domain.sectional.tournament.PlayerEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.PlayerDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.PlayerLinkDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.shortcut.PlayerShortDTO;
import ru.vldf.sportsportal.mapper.general.AbstractOverallRightsBasedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.UserURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.tournament.PlayerURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureLinkMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(uses = {UserMapper.class, PictureLinkMapper.class, PlayerURLMapper.class, UserURLMapper.class, PictureURLMapper.class, JavaTimeMapper.class})
public abstract class PlayerMapper extends AbstractOverallRightsBasedMapper<PlayerEntity, PlayerDTO, PlayerShortDTO, PlayerLinkDTO> {

    @Mappings({
            @Mapping(target = "user", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
            @Mapping(target = "avatar", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    })
    public abstract PlayerEntity toEntity(PlayerDTO dto);


    @Mappings({
            @Mapping(target = "playerURL", source = "id", qualifiedByName = {"toPlayerURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"})
    })
    public abstract PlayerLinkDTO toLinkDTO(PlayerEntity entity);


    @Mappings({
            @Mapping(target = "playerURL", source = "id", qualifiedByName = {"toPlayerURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"})
    })
    public abstract PlayerShortDTO toShortDTO(PlayerEntity entity);


    @Override
    public PlayerEntity merge(PlayerEntity acceptor, PlayerEntity donor) throws OptimisticLockException {
        super.merge(acceptor, donor);
        acceptor.setName(donor.getName());
        acceptor.setSurname(donor.getSurname());
        acceptor.setPatronymic(donor.getPatronymic());
        acceptor.setBirthdate(donor.getBirthdate());
        return acceptor;
    }
}
