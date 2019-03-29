package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "tour_bundle_structure", schema = "tournament")
public class TourBundleStructureEntity extends AbstractDictionaryEntity {

    @Basic
    @Column(name = "tourless", nullable = false)
    private boolean tourless = false;

    @Basic
    @Column(name = "immutable", nullable = false)
    private boolean immutable = false;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourBundleStructureEntity)) return false;
        return super.equals(o);
    }
}
