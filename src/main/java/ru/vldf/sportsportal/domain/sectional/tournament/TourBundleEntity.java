package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "tour_bundle", schema = "tournament", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"parent_id", "numeric_label"}),
        @UniqueConstraint(columnNames = {"parent_id", "text_label"})
})
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
    @JoinColumn(name = "bundle_type_id", referencedColumnName = "id", nullable = false)
    private TourBundleTypeEntity bundleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_structure_id", referencedColumnName = "id", nullable = false)
    private TourBundleStructureEntity bundleStructure;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", referencedColumnName = "id")
    private TournamentEntity tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private TourBundleEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Collection<TourBundleEntity> child;

    @OneToMany(mappedBy = "bundle", cascade = CascadeType.ALL)
    private Collection<TourEntity> tours;
}
