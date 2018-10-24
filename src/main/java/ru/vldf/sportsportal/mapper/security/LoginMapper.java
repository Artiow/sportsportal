package ru.vldf.sportsportal.mapper.security;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.security.core.userdetails.User;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.dto.security.LoginDTO;
import ru.vldf.sportsportal.mapper.manual.url.common.PictureURLMapper;
import ru.vldf.sportsportal.mapper.manual.url.common.UserURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.RoleMapper;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUser;

import java.util.ArrayList;

@Mapper(
        componentModel = "spring",
        uses = {UserURLMapper.class, PictureURLMapper.class, RoleMapper.class}
)
@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
public interface LoginMapper {

    @Mappings({
            @Mapping(target = "userURL", source = "id", qualifiedByName = {"toUserURL", "fromId"}),
            @Mapping(target = "avatarURL", source = "avatar", qualifiedByName = {"toPictureURL", "fromEntity"}),
            @Mapping(target = "username", source = "name")
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
                        .username(entity.getEmail())
                        .password(entity.getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build()
        );
    }
}
