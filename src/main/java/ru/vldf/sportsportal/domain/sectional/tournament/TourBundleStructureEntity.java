package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "tour_bundle_structure", schema = "tournament")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class TourBundleStructureEntity extends AbstractDictionaryEntity {

    @Basic
    @Column(name = "tourless", nullable = false)
    private boolean tourless = false;

    @Basic
    @Column(name = "immutable", nullable = false)
    private boolean immutable = false;


    @OneToMany(mappedBy = "bundleStructure")
    private Collection<TourBundleEntity> tourBundles;
}
