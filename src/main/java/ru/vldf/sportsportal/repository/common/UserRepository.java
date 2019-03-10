package ru.vldf.sportsportal.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

/**
 * @author Namednev Artem
 */
public interface UserRepository extends AbstractIdentifiedRepository<UserEntity> {

    @Query("select case when (count(u) > 0) then true else false end from UserEntity u where (u.email = :email) and (u.isDisabled = false)")
    boolean isEnabled(@Param("email") String email);


    UserEntity findByConfirmCode(String confirmCode);

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);
}
