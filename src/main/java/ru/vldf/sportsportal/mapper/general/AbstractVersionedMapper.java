package ru.vldf.sportsportal.mapper.general;

import org.hibernate.proxy.HibernateProxy;
import ru.vldf.sportsportal.domain.general.AbstractVersionedEntity;
import ru.vldf.sportsportal.dto.general.VersionedDTO;

import javax.persistence.OptimisticLockException;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
public abstract class AbstractVersionedMapper<E extends AbstractVersionedEntity, D extends VersionedDTO> extends AbstractIdentifiedMapper<E, D> {

    @Override
    public E merge(E acceptor, E donor) throws OptimisticLockException {
        versionCheck(acceptor, donor);
        return super.merge(acceptor, donor);
    }

    private void versionCheck(E acceptor, E donor) throws OptimisticLockException {
        if (!Objects.equals(acceptor.getVersion(), donor.getVersion())) {
            throw new OptimisticLockException(
                    String.format(
                            "%s failed merge for %s because the entity versions does not match",
                            this.getClass().getName(),
                            getEntityClassName(acceptor)
                    )
            );
        }
    }

    private String getEntityClassName(E acceptor) {
        return ((acceptor instanceof HibernateProxy) ? ((HibernateProxy) acceptor).getHibernateLazyInitializer().getPersistentClass() : acceptor.getClass()).getName();
    }
}
