package ru.vldf.sportsportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.vldf.sportsportal.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {

    UserEntity findByLogin(String login);

    boolean existsByLogin(String login);
}
