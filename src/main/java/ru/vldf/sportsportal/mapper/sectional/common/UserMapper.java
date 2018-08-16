package ru.vldf.sportsportal.mapper.sectional.common;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.manual.uri.common.PictureURIMapper;
import ru.vldf.sportsportal.mapper.manual.uri.common.UserURIMapper;

import javax.persistence.OptimisticLockException;

@Mapper(
        componentModel = "spring",
        uses = {UserURIMapper.class, PictureURIMapper.class, PictureMapper.class, RoleMapper.class}
)
public interface UserMapper extends AbstractVersionedMapper<UserEntity, UserDTO> {

    @Mappings({
            @Mapping(target = "userURI", source = "id", qualifiedByName = {"toUserURI", "fromId"}),
            @Mapping(target = "avatarURI", source = "avatar", qualifiedByName = {"toPictureURI", "fromEntity"})
    })
    UserShortDTO toShortDTO(UserEntity entity);

    @Mapping(target = "uri", source = "id", qualifiedByName = {"toUserURI", "fromId"})
    UserDTO toDTO(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserDTO dto);

    @Override
    default UserEntity merge(UserEntity acceptor, UserEntity donor) throws OptimisticLockException {
        // todo: merge!
        return acceptor;
    }
}
