package ru.vldf.sportsportal.mapper.generic;

import org.hibernate.proxy.HibernateProxy;
import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.dto.generic.AbstractVersionedDTO;

import javax.persistence.OptimisticLockException;

public interface AbstractVersionedMapper<E extends AbstractVersionedEntity, D extends AbstractVersionedDTO> extends AbstractIdentifiedMapper<E, D> {

    @Override
    default E merge(E acceptor, E donor) throws OptimisticLockException {
        check(acceptor, donor);
        acceptor.setVersion(donor.getVersion());
        return acceptor;
    }

    default void check(AbstractVersionedEntity oldObject, AbstractVersionedEntity newObject) throws OptimisticLockException {
        Long oldVersion = oldObject.getVersion();
        Long newVersion = newObject.getVersion();
        if (!oldVersion.equals(newVersion)) {
            throw new OptimisticLockException(String.format(
                    "%s failed merge for %s because the entity versions do not match",
                    this.getClass().getName(),
                    ((oldObject instanceof HibernateProxy)
                            ? ((HibernateProxy) oldObject).getHibernateLazyInitializer().getPersistentClass()
                            : oldObject.getClass())
                            .getName()
            ));
        }
    }
}
