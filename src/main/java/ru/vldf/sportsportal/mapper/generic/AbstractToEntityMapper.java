package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.util.Collection;
import java.util.List;

public interface AbstractToEntityMapper<E extends DomainObject, D extends DataTransferObject> {

    E toEntity(D dto);

    Collection<E> toEntity(List<D> dtoCollection);
}
