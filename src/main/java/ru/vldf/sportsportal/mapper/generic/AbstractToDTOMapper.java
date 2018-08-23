package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.util.Collection;
import java.util.List;

public interface AbstractToDTOMapper<E extends DomainObject, D extends DataTransferObject> {

    D toDTO(E entity);

    List<D> toDTO(Collection<E> entityCollection);
}
