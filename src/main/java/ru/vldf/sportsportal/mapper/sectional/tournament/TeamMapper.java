package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.*;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TeamDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TeamLinkDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.shortcut.TeamShortDTO;
import ru.vldf.sportsportal.mapper.general.AbstractOverallMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.UserURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.tournament.TeamURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.PictureLinkMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, PictureLinkMapper.class, TeamURLMapper.class, UserURLMapper.class, PictureURLMapper.class}
)
public abstract class TeamMapper extends AbstractOverallMapper<TeamEntity, TeamDTO, TeamShortDTO, TeamLinkDTO> {

    @Mappings({
            @Mapping(target = "isLocked", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
            @Mapping(target = "isDisabled", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    })
    public abstract TeamEntity toEntity(TeamDTO dto);


    @Mappings({
            @Mapping(target = "teamURL", source = "id", qualifiedByName = {"toTeamURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"})
    })
    public abstract TeamLinkDTO toLinkDTO(TeamEntity entity);

    @Mappings({
            @Mapping(target = "name", ignore = true)
    })
    public abstract TeamEntity toLinkEntity(TeamLinkDTO dto);


    @Mappings({
            @Mapping(target = "teamURL", source = "id", qualifiedByName = {"toTeamURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"}),
            @Mapping(target = "mainCaptainURL", source = "mainCaptain", qualifiedByName = {"toUserURL", "fromEntity"}),
            @Mapping(target = "viceCaptainURL", source = "viceCaptain", qualifiedByName = {"toUserURL", "fromEntity"})
    })
    public abstract TeamShortDTO toShortDTO(TeamEntity entity);


    @Override
    public TeamEntity inject(TeamEntity acceptor, TeamDTO donor) {
        // params by default saving
        boolean lockedByDefault = acceptor.getIsLocked();
        boolean disabledByDefault = acceptor.getIsDisabled();

        // inject
        acceptor = super.inject(acceptor, donor);

        // params by default restoring
        if (Objects.isNull(donor.getIsLocked())) {
            acceptor.setIsLocked(lockedByDefault);
        }
        if (Objects.isNull(donor.getIsDisabled())) {
            acceptor.setIsDisabled(disabledByDefault);
        }

        return acceptor;
    }

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
