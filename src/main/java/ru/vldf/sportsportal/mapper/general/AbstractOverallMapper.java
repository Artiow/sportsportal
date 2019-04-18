package ru.vldf.sportsportal.mapper.general;

import ru.vldf.sportsportal.domain.general.AbstractVersionedEntity;
import ru.vldf.sportsportal.dto.general.VersionedDTO;
import ru.vldf.sportsportal.dto.general.VersionedLinkDTO;
import ru.vldf.sportsportal.dto.general.VersionedShortDTO;

/**
 * @author Namednev Artem
 */
public abstract class AbstractOverallMapper<E extends AbstractVersionedEntity, D extends VersionedDTO, S extends VersionedShortDTO, L extends VersionedLinkDTO> extends AbstractVersionedMapper<E, D> implements ShortMapper<E, S>, LinkMapper<E, L> {

}
