package ru.vldf.sportsportal.domain.generic;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractVersionedEntity extends AbstractIdentifiedEntity {

    @Version
    @Column(name = "version")
    private Long version = 0L;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractVersionedEntity)) return false;
        if (!super.equals(o)) return false;
        AbstractVersionedEntity that = (AbstractVersionedEntity) o;
        return Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVersion());
    }
}
