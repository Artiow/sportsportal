package ru.vldf.sportsportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.vldf.sportsportal.domain.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer>, JpaSpecificationExecutor<RoleEntity> {

}
