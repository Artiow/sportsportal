package ru.vldf.sportsportal.service.security;

import org.springframework.data.util.Pair;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

/**
 * @author Namednev Artem
 */
public interface SecurityProvider {

    /**
     * Returns token pair (access and refresh) for user.
     *
     * @param user the user entity.
     * @return token pair (access and refresh).
     */
    Pair<String, String> login(UserEntity user);
}
