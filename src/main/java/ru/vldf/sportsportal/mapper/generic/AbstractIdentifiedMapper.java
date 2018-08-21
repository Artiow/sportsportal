package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import java.util.Collection;
import java.util.List;

public interface AbstractIdentifiedMapper<E extends AbstractIdentifiedEntity, D extends AbstractIdentifiedDTO> {

    D toDTO(E entity);

    List<D> toDTO(Collection<E> entityCollection);

    E toEntity(D dto);

    Collection<E> toEntity(List<D> dtoCollection);

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
