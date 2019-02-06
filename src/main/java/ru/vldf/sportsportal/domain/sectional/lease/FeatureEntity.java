package ru.vldf.sportsportal.domain.sectional.lease;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "feature", schema = "lease")
public class FeatureEntity extends AbstractDictionaryEntity {

    @ManyToMany(mappedBy = "capabilities")
    private Collection<PlaygroundEntity> playgrounds;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureEntity)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
