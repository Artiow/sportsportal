package ru.vldf.sportsportal.repository.common;

import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

/**
 * @author Namednev Artem
 */
public interface UserRepository extends AbstractIdentifiedRepository<UserEntity> {

    UserEntity findByConfirmCode(String confirmCode);

    UserEntity findByEmail(String email);

    boolean existsByEmailAndIsDisabledIsFalse(String email);

    boolean existsByEmail(String email);
}
