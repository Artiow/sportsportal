package ru.vldf.sportsportal.mapper.generic;

import org.hibernate.proxy.HibernateProxy;
import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.dto.generic.VersionedDTO;

import javax.persistence.OptimisticLockException;

/**
 * @author Namednev Artem
 */
public interface AbstractVersionedMapper<E extends AbstractVersionedEntity, D extends VersionedDTO> extends AbstractIdentifiedMapper<E, D> {

    @Override
    default E merge(E acceptor, E donor) throws OptimisticLockException {
        check(acceptor, donor);
        acceptor.setVersion(donor.getVersion());
        return acceptor;
    }

    default void check(AbstractVersionedEntity acceptor, AbstractVersionedEntity donor) throws OptimisticLockException {
        Long oldVersion = acceptor.getVersion();
        Long newVersion = donor.getVersion();
        if (newVersion != null) {
            if (oldVersion == null) {
                throw new IllegalArgumentException(String.format(
                        "%s failed merge for %s because the acceptor entity has no version, but donor version present",
                        this.getClass().getName(), ((acceptor instanceof HibernateProxy) ? ((HibernateProxy) acceptor).getHibernateLazyInitializer().getPersistentClass() : acceptor.getClass()).getName()
                ));
            } else if (!oldVersion.equals(newVersion)) {
                throw new OptimisticLockException(String.format(
                        "%s failed merge for %s because the entity versions do not match",
                        this.getClass().getName(), ((acceptor instanceof HibernateProxy) ? ((HibernateProxy) acceptor).getHibernateLazyInitializer().getPersistentClass() : acceptor.getClass()).getName()
                ));
            }
        }
    }
}
