package ru.vldf.sportsportal.mapper.security;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.security.core.userdetails.User;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.security.LoginDTO;
import ru.vldf.sportsportal.mapper.sectional.common.RoleMapper;
import ru.vldf.sportsportal.mapper.manual.uri.common.PictureURIMapper;
import ru.vldf.sportsportal.mapper.manual.uri.common.UserURIMapper;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUser;

import java.util.ArrayList;

@Mapper(
        componentModel = "spring",
        uses = {UserURIMapper.class, PictureURIMapper.class, RoleMapper.class}
)
public interface LoginMapper {

    @Mappings({
            @Mapping(target = "userURI", source = "id", qualifiedByName = {"UserURIMapper", "toUserURI"}),
            @Mapping(target = "avatarURI", source = "avatar.id", qualifiedByName = {"PictureURIMapper", "toPictureURI"}),
            @Mapping(target = "roles", source = "roles")
    })
    LoginDTO toLoginDTO(UserEntity entity);

    default IdentifiedUser toIdentifiedUser(UserEntity entity) {
        ArrayList<String> roles = new ArrayList<>(entity.getRoles().size());
        for (RoleEntity roleEntity : entity.getRoles()) {
            roles.add(roleEntity.getCode().toUpperCase());
        }

        return new IdentifiedUser(
                entity.getId(),
                User.builder()
                        .username(entity.getLogin())
                        .password(entity.getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build()
        );
    }
}
