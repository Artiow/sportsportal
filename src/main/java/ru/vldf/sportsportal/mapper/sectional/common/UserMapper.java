package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.sectional.common.links.UserLinkDTO;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.mapper.general.AbstractOverallRightsBasedMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.UserURLMapper;

import javax.persistence.OptimisticLockException;

/**
 * @author Namednev Artem
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(uses = {RoleMapper.class, PictureLinkMapper.class, PictureURLMapper.class, UserURLMapper.class})
public abstract class UserMapper extends AbstractOverallRightsBasedMapper<UserEntity, UserDTO, UserShortDTO, UserLinkDTO> {

    @Mappings({
            @Mapping(target = "userURL", source = "id", qualifiedByName = {"toUserURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"})
    })
    public abstract UserLinkDTO toLinkDTO(UserEntity entity);


    @Mappings({
            @Mapping(target = "userURL", source = "id", qualifiedByName = {"toUserURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"})
    })
    public abstract UserShortDTO toShortDTO(UserEntity entity);


    @Override
    public UserEntity merge(UserEntity acceptor, UserEntity donor) throws OptimisticLockException {
        super.merge(acceptor, donor);
        acceptor.setEmail(donor.getEmail());
        acceptor.setPassword(donor.getPassword());
        acceptor.setName(donor.getName());
        acceptor.setSurname(donor.getSurname());
        acceptor.setPatronymic(donor.getPatronymic());
        acceptor.setAddress(donor.getAddress());
        acceptor.setPhone(donor.getPhone());
        acceptor.setConfirmCode(donor.getConfirmCode());
        acceptor.setAvatar(donor.getAvatar());
        return acceptor;
    }
}
