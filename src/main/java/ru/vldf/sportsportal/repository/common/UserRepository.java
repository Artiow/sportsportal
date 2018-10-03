package ru.vldf.sportsportal.repository.common;

import org.springframework.data.jpa.repository.Query;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

public interface UserRepository extends AbstractIdentifiedRepository<UserEntity> {

    UserEntity findByLogin(String login);

    boolean existsByLogin(String login);

    @Query("select case when (count(u) > 0) then true else false end from UserEntity u join u.roles r where (u.id = :id) and (r.code = :code)")
    boolean existsByIdAndRoleCode(Integer id, String code);
}
