package ru.vldf.sportsportal.domain.generic;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractVersionedEntity extends AbstractIdentifiedEntity {

    @Version
    @Column(name = "version")
    private Long version = 0L;


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


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
