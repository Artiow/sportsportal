package ru.vldf.sportsportal.domain.generic;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractIdentifiedEntity implements DomainObject {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractIdentifiedEntity)) return false;
        AbstractIdentifiedEntity that = (AbstractIdentifiedEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
