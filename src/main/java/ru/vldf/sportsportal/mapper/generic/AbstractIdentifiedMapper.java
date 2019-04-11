package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.dto.generic.IdentifiedDTO;

/**
 * @author Namednev Artem
 */
public abstract class AbstractIdentifiedMapper<E extends AbstractIdentifiedEntity, D extends IdentifiedDTO> extends AbstractModelMapper<E, D> {

    public Integer toInteger(E entity) {
        return entity.getId();
    }

    public Integer toInteger(D dto) {
        return dto.getId();
    }

    public E inject(E acceptor, D donor) {
        return merge(acceptor, toEntity(donor));
    }

    public E merge(E acceptor, E donor) {
        return acceptor;
    }
}
