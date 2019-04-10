package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.LinkedDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author Namednev Artem
 */
public interface LinkMapper<E extends DomainObject, D extends LinkedDTO> extends ModelForwardMapper<E, D> {

    default D toLinkDTO(E entity) {
        return toDTO(entity);
    }

    default List<D> toLinkDTO(Collection<E> entityCollection) {
        return toDTO(entityCollection);
    }
}
