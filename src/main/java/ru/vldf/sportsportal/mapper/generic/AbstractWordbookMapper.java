package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.AbstractWordbookEntity;
import ru.vldf.sportsportal.dto.generic.AbstractWordbookDTO;

public interface AbstractWordbookMapper<E extends AbstractWordbookEntity, D extends AbstractWordbookDTO> extends AbstractIdentifiedMapper<E, D> {

    default String toString(E entity) {
        return entity.getCode();
    }

    default String toString(D dto) {
        return dto.getCode();
    }
}
