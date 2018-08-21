package ru.vldf.sportsportal.repository.common;

import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;
import ru.vldf.sportsportal.repository.AbstractRepository;

import java.util.Collection;

public interface RoleRepository extends AbstractRepository<RoleEntity, Integer> {

    Collection<RoleEntity> findAllByCode(String code);
}
