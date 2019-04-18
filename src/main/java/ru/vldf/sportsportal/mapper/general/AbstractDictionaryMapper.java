package ru.vldf.sportsportal.mapper.general;

import ru.vldf.sportsportal.domain.general.AbstractDictionaryEntity;
import ru.vldf.sportsportal.dto.general.DictionaryDTO;

/**
 * @author Namednev Artem
 */
public abstract class AbstractDictionaryMapper<E extends AbstractDictionaryEntity, D extends DictionaryDTO> extends AbstractWordbookMapper<E, D> {

    public E merge(E acceptor, E donor) {
        super.merge(acceptor, donor);
        acceptor.setDescription(donor.getDescription());
        return acceptor;
    }
}
