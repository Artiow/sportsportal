package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

public interface AbstractIdentifiedMapper<E extends AbstractIdentifiedEntity, D extends AbstractIdentifiedDTO> extends ObjectMapper<E, D> {

    default Integer toInteger(E entity) {
        return entity.getId();
    }

    default Integer toInteger(D dto) {
        return dto.getId();
    }

    default E merge(E acceptor, E donor) {
        return acceptor;
    }
}
