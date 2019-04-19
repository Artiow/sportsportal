package ru.vldf.sportsportal.util.domain;

import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.PlayerEntity;

import java.util.Objects;

/**
 * @author Artem Namednev
 */
public final class PlayerBindingChecker {

    public static boolean match(PlayerEntity playerEntity, UserEntity userEntity) {
        return ((playerEntity == null) || (userEntity == null)) || (Objects.equals(playerEntity.getName(), userEntity.getName()) && Objects.equals(playerEntity.getSurname(), userEntity.getSurname()) && Objects.equals(playerEntity.getPatronymic(), userEntity.getPatronymic()));
    }
}
