package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

public interface AbstractMapper<E extends DomainObject, D extends DataTransferObject> extends AbstractToDTOMapper<E, D>, AbstractToEntityMapper<E, D> {

}
