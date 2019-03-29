package ru.vldf.sportsportal.domain.sectional.lease;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "sport", schema = "lease")
public class SportEntity extends AbstractDictionaryEntity {

    @ManyToMany(mappedBy = "specializations")
    private Collection<PlaygroundEntity> playgrounds;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SportEntity)) return false;
        return super.equals(o);
    }
}
