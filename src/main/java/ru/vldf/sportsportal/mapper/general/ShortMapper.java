package ru.vldf.sportsportal.mapper.general;

import ru.vldf.sportsportal.domain.general.root.DomainObject;
import ru.vldf.sportsportal.dto.general.ShortedDTO;
import ru.vldf.sportsportal.mapper.general.root.ModelForwardMapper;

import java.util.Collection;
import java.util.List;

/**
 * @author Namednev Artem
 */
public interface ShortMapper<E extends DomainObject, D extends ShortedDTO> extends ModelForwardMapper {

    D toShortDTO(E entity);

    List<D> toShortDTO(Collection<E> entityCollection);
}
