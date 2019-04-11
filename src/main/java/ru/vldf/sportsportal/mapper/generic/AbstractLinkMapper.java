package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.DomainObject;
import ru.vldf.sportsportal.dto.generic.LinkedDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author Namednev Artem
 */
public abstract class AbstractLinkMapper<E extends DomainObject, D extends LinkedDTO> implements ModelForwardMapper<E, D> {

    public abstract D toLinkDTO(E entity);

    public abstract List<D> toLinkDTO(Collection<E> entityCollection);
}
