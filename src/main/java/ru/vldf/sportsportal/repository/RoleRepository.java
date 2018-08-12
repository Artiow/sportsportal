package ru.vldf.sportsportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.vldf.sportsportal.domain.sectional.common.RoleEntity;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer>, JpaSpecificationExecutor<RoleEntity> {

    Collection<RoleEntity> findAllByCode(String code);
}
