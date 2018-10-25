package ru.vldf.sportsportal.mapper.security;

import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

@Component
public class UserDetailsMapper {

    public UserEntity toEntity(IdentifiedUserDetails details) {
        throw new UnsupportedOperationException();
    }

    public IdentifiedUserDetails toDetails(UserEntity entity) {
        throw new UnsupportedOperationException();
    }
}
