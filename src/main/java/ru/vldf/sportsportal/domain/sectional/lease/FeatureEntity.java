package ru.vldf.sportsportal.domain.sectional.lease;

import lombok.EqualsAndHashCode;
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
@Table(name = "feature", schema = "lease")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class FeatureEntity extends AbstractDictionaryEntity {

    @ManyToMany(mappedBy = "capabilities")
    private Collection<PlaygroundEntity> playgrounds;
}
