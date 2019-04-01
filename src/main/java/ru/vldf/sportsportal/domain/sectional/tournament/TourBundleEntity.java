package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;

import javax.persistence.*;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "tour_bundle", schema = "tournament")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class TourBundleEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "text_label", nullable = false)
    private String textLabel;

    @Basic
    @Column(name = "numeric_label", nullable = false)
    private Integer numericLabel;

    @Basic
    @Column(name = "completed", nullable = false)
    private Boolean isCompleted = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private TourBundleEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_type_id", referencedColumnName = "id", nullable = false)
    private TourBundleTypeEntity bundleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_structure_id", referencedColumnName = "id", nullable = false)
    private TourBundleStructureEntity bundleStructure;
}
