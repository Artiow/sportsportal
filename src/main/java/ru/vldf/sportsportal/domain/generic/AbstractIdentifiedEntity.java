package ru.vldf.sportsportal.domain.generic;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractIdentifiedEntity implements DomainObject {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractIdentifiedEntity)) return false;

        AbstractIdentifiedEntity that = (AbstractIdentifiedEntity) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
