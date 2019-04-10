package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

/**
 * @author Namednev Artem
 */
public interface ModelBidirectionalMapper<E extends DomainObject, D extends DataTransferObject> extends ModelForwardMapper<E, D>, ModelReverseMapper<E, D> {

}
