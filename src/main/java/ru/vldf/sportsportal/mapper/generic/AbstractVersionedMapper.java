package ru.vldf.sportsportal.mapper.generic;

import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.dto.generic.AbstractVersionedDTO;

import javax.persistence.OptimisticLockException;

public interface AbstractVersionedMapper<E extends AbstractVersionedEntity, D extends AbstractVersionedDTO> extends AbstractMapper<E, D> {

    default E merge(E acceptor, E donor) throws OptimisticLockException {
        Long oldVersion = acceptor.getVersion();
        Long newVersion = donor.getVersion();

        if (!oldVersion.equals(newVersion)) {
            throw new OptimisticLockException(acceptor.getClass().getName());
        }

        acceptor.setVersion(newVersion);
        return acceptor;
    }

    default void check(AbstractVersionedEntity oldObject, AbstractVersionedEntity newObject) throws OptimisticLockException {
        Long oldVersion = oldObject.getVersion();
        Long newVersion = newObject.getVersion();

        if (!oldVersion.equals(newVersion)) {
            throw new OptimisticLockException(oldObject.getClass().getName());
        }
    }
}
