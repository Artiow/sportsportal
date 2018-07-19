package ru.vldf.sportsportal.domain.generic;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractVersionedEntity extends AbstractIdentifiedEntity {

    @Version
    @Column(name = "version")
    private Long version = 1L;


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
