package ru.vldf.sportsportal.repository.common;

import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.repository.AbstractRepository;

public interface PictureRepository extends AbstractRepository<PictureEntity, Integer> {

    boolean existsById(Integer id);
}
