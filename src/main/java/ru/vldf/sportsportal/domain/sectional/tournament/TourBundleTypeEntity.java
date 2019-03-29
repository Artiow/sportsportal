package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "tour_bundle_type", schema = "tournament")
public class TourBundleTypeEntity extends AbstractDictionaryEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourBundleTypeEntity)) return false;
        return super.equals(o);
    }
}
