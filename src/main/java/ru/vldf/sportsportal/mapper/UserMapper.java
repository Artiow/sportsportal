package ru.vldf.sportsportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.RoleEntity;
import ru.vldf.sportsportal.domain.UserEntity;
import ru.vldf.sportsportal.dto.UserDTO;
import ru.vldf.sportsportal.dto.security.LoginDTO;
import ru.vldf.sportsportal.dto.shortcut.UserShortDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractMapper;
import ru.vldf.sportsportal.mapper.manual.uri.UserURIMapper;

@Mapper(uses = {UserURIMapper.class}, componentModel = "spring")
public interface UserMapper extends AbstractMapper<UserEntity, UserDTO> {

    @Mapping(target = "userURI", source = "id")
    LoginDTO toLoginDTO(UserEntity entity);

    UserShortDTO toShortDTO(UserEntity entity);

    default String toString(RoleEntity entity) {
        return entity.getCode();
    }

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserDTO dto);
}
