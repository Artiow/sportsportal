package ru.vldf.sportsportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.UserDTO;
import ru.vldf.sportsportal.dto.shortcut.UserShortDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractMapper;
import ru.vldf.sportsportal.mapper.manual.uri.PictureURIMapper;
import ru.vldf.sportsportal.mapper.manual.uri.UserURIMapper;

@Mapper(
        componentModel = "spring",
        uses = {UserURIMapper.class, PictureURIMapper.class, PictureMapper.class, RoleMapper.class}
)
public interface UserMapper extends AbstractMapper<UserEntity, UserDTO> {

    @Mappings({
            @Mapping(target = "userURI", source = "id", qualifiedByName = {"UserURIMapper", "toUserURI"}),
            @Mapping(target = "avatarURI", source = "avatar.id", qualifiedByName = {"PictureURIMapper", "toPictureURI"}),
            @Mapping(target = "roles", source = "roles")
    })
    UserShortDTO toShortDTO(UserEntity entity);

    @Mapping(target = "uri", source = "id", qualifiedByName = {"UserURIMapper", "toUserURI"})
    UserDTO toDTO(UserEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "avatar", ignore = true),
            @Mapping(target = "roles", ignore = true)
    })
    UserEntity toEntity(UserDTO dto);
}
