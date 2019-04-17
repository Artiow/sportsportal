package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.general.AbstractVersionedEntity;
import ru.vldf.sportsportal.dto.generic.VersionedDTO;
import ru.vldf.sportsportal.dto.generic.VersionedLinkDTO;
import ru.vldf.sportsportal.dto.generic.VersionedShortDTO;

/**
 * @author Namednev Artem
 */
public abstract class AbstractOverallMapper<E extends AbstractVersionedEntity, D extends VersionedDTO, S extends VersionedShortDTO, L extends VersionedLinkDTO> extends AbstractVersionedMapper<E, D> implements ShortMapper<E, S>, LinkMapper<E, L> {

}
