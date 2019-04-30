package ru.vldf.sportsportal.mapper.general;

import ru.vldf.sportsportal.domain.general.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.dto.general.IdentifiedDTO;
import ru.vldf.sportsportal.dto.general.IdentifiedLinkDTO;
import ru.vldf.sportsportal.dto.general.IdentifiedShortDTO;

/**
 * @author Namednev Artem
 */
public abstract class AbstractOverallIdentifiedMapper<E extends AbstractIdentifiedEntity, D extends IdentifiedDTO, S extends IdentifiedShortDTO, L extends IdentifiedLinkDTO>
        extends AbstractIdentifiedMapper<E, D> implements ShortMapper<E, S>, LinkMapper<E, L> {

}
