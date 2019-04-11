package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.util.Collection;
import java.util.List;

/**
 * @author Namednev Artem
 */
public interface BasicMapper<E extends DomainObject, D extends DataTransferObject> extends ModelBidirectionalMapper {

    D toDTO(E entity);

    List<D> toDTO(Collection<E> entityCollection);

    E toEntity(D dto);

    Collection<E> toEntity(List<D> dtoCollection);
}
