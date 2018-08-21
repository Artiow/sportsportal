package ru.vldf.sportsportal.repository.common;

import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.AbstractRepository;

public interface UserRepository extends AbstractRepository<UserEntity, Integer> {

    UserEntity findByLogin(String login);

    boolean existsByLogin(String login);
}
