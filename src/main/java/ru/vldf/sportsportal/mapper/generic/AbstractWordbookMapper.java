package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.AbstractWordbookEntity;
import ru.vldf.sportsportal.dto.generic.WordbookDTO;

public interface AbstractWordbookMapper<E extends AbstractWordbookEntity, D extends WordbookDTO> extends AbstractIdentifiedMapper<E, D> {

    default String toString(E entity) {
        return entity.getCode();
    }

    default String toString(D dto) {
        return dto.getCode();
    }

    default E merge(E acceptor, E donor) {
        acceptor.setCode(donor.getCode());
        acceptor.setName(donor.getName());
        return acceptor;
    }
}
