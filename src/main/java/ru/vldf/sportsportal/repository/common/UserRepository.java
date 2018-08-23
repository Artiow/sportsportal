package ru.vldf.sportsportal.repository.common;

import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

public interface UserRepository extends AbstractIdentifiedRepository<UserEntity> {

    UserEntity findByLogin(String login);

    boolean existsByLogin(String login);
}
