package ru.vldf.sportsportal.mapper.manual.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.service.security.userdetails.model.IdentifiedUser;
import ru.vldf.sportsportal.service.security.userdetails.model.IdentifiedUserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Component
public class UserDetailsMapper {

    public IdentifiedUserDetails toDetails(UserEntity entity) {
        Collection<RoleEntity> roleEntities = entity.getRoles();
        Collection<String> rawRoles = new ArrayList<>(roleEntities.size());
        for (RoleEntity roleEntity : roleEntities) {
            rawRoles.add(roleEntity.getCode().trim().toUpperCase());
        }

        String[] roles = rawRoles.toArray(new String[0]);
        return new IdentifiedUser(entity.getId(), User.builder()
                .username(entity.getEmail())
                .password(entity.getPassword())
                .accountLocked(entity.getIsLocked())
                .disabled(entity.getIsDisabled())
                .roles(roles)
                .build()
        );
    }
}
