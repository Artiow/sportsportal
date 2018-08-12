package ru.vldf.sportsportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;

public interface PictureRepository extends JpaRepository<PictureEntity, Integer>, JpaSpecificationExecutor<PictureEntity> {

    boolean existsById(Integer id);
}
