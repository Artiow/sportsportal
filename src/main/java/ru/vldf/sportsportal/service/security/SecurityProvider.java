package ru.vldf.sportsportal.service.security;

import org.springframework.data.util.Pair;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

public interface SecurityProvider {

    /**
     * Returns pair (access and refresh) of JWT by user.
     *
     * @param userEntity {@link UserEntity} user
     * @return {@link Pair} pair of jwt
     */
    Pair<String, String> authorization(UserEntity userEntity);

    /**
     * Returns pair (access and refresh) of JWT by refresh JWT.
     *
     * @param refreshToken {@link String} refresh jwt
     * @return {@link Pair} pair of jwt
     */
    Pair<String, String> authorization(String refreshToken);

    /**
     * Returns IdentifiedUserDetails by JWT.
     *
     * @param accessToken {@link String} access jwt
     * @return {@link IdentifiedUserDetails} user details
     */
    IdentifiedUserDetails authentication(String accessToken);
}
