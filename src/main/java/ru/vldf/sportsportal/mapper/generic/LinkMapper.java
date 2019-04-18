package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.general.root.DomainObject;
import ru.vldf.sportsportal.dto.general.LinkedDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author Namednev Artem
 */
public interface LinkMapper<E extends DomainObject, D extends LinkedDTO> extends ModelBidirectionalMapper {

    D toLinkDTO(E entity);

    List<D> toLinkDTO(Collection<E> entityCollection);

    E toLinkEntity(D dto);

    Collection<E> toLinkEntity(List<D> dtoCollection);
}
