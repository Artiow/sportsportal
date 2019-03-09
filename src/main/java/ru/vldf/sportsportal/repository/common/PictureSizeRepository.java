package ru.vldf.sportsportal.repository.common;

import ru.vldf.sportsportal.domain.sectional.common.PictureSizeEntity;
import ru.vldf.sportsportal.repository.AbstractWordbookRepository;

/**
 * @author Namednev Artem
 */
public interface PictureSizeRepository extends AbstractWordbookRepository<PictureSizeEntity> {

    PictureSizeEntity findFirstByIsDefaultIsTrue();
}
