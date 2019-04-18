package ru.vldf.sportsportal.mapper.general;

import org.hibernate.proxy.HibernateProxy;
import ru.vldf.sportsportal.domain.general.AbstractVersionedEntity;
import ru.vldf.sportsportal.dto.general.VersionedDTO;

import javax.persistence.OptimisticLockException;

/**
 * @author Namednev Artem
 */
public abstract class AbstractVersionedMapper<E extends AbstractVersionedEntity, D extends VersionedDTO> extends AbstractIdentifiedMapper<E, D> {

    @Override
    public E merge(E acceptor, E donor) throws OptimisticLockException {
        versionCheck(acceptor, donor);
        acceptor.setVersion(donor.getVersion());
        return acceptor;
    }

    private void versionCheck(AbstractVersionedEntity acceptor, AbstractVersionedEntity donor) throws OptimisticLockException {
        Long oldVersion = acceptor.getVersion();
        Long newVersion = donor.getVersion();
        if (newVersion != null) {
            if (oldVersion == null) {
                throw new IllegalArgumentException(exMessage("%s failed merge for %s because the acceptor entity has no version, but donor version present", acceptor));
            } else if (!oldVersion.equals(newVersion)) {
                throw new OptimisticLockException(exMessage("%s failed merge for %s because the entity versions does not match", acceptor));
            }
        }
    }

    private String exMessage(String template, AbstractVersionedEntity acceptor) {
        return String.format(template, this.getClass().getName(), ((acceptor instanceof HibernateProxy) ? ((HibernateProxy) acceptor).getHibernateLazyInitializer().getPersistentClass() : acceptor.getClass()).getName());
    }
}
