package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.general.AbstractWordbookEntity;
import ru.vldf.sportsportal.dto.generic.WordbookDTO;

/**
 * @author Namednev Artem
 */
public abstract class AbstractWordbookMapper<E extends AbstractWordbookEntity, D extends WordbookDTO> extends AbstractIdentifiedMapper<E, D> {

    public String toString(E entity) {
        return entity.getCode();
    }

    public String toString(D dto) {
        return dto.getCode();
    }

    public E merge(E acceptor, E donor) {
        acceptor.setCode(donor.getCode());
        acceptor.setName(donor.getName());
        return acceptor;
    }
}
