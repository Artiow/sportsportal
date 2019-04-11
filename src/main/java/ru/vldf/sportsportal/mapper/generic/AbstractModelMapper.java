package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.util.Collection;
import java.util.List;

/**
 * @author Namednev Artem
 */
public abstract class AbstractModelMapper<E extends DomainObject, D extends DataTransferObject> implements ModelBidirectionalMapper<E, D> {

    public abstract D toDTO(E entity);

    public abstract List<D> toDTO(Collection<E> entityCollection);

    public abstract E toEntity(D dto);

    public abstract Collection<E> toEntity(List<D> dtoCollection);
}
