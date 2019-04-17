package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.general.DomainObject;
import ru.vldf.sportsportal.dto.generic.ShortedDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author Namednev Artem
 */
public interface ShortMapper<E extends DomainObject, D extends ShortedDTO> extends ModelForwardMapper {

    D toShortDTO(E entity);

    List<D> toShortDTO(Collection<E> entityCollection);
}
