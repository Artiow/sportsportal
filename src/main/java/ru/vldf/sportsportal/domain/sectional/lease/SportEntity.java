package ru.vldf.sportsportal.domain.sectional.lease;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.AbstractDictionaryEntity;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class SportEntity extends AbstractDictionaryEntity {

    @ManyToMany(mappedBy = "specializations")
    private Collection<PlaygroundEntity> playgrounds;
}
