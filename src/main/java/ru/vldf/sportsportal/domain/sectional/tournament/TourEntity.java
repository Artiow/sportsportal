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
@Table(name = "tour", schema = "tournament", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"bundle_id", "numeric_label"}),
        @UniqueConstraint(columnNames = {"bundle_id", "text_label"})
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class TourEntity extends AbstractIdentifiedEntity {

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
    @JoinColumn(name = "bundle_id", referencedColumnName = "id", nullable = false)
    private TourBundleEntity bundle;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private Collection<GameEntity> games;
}
