package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "tour", schema = "tournament")
public class TourEntity extends AbstractIdentifiedEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourEntity)) return false;
        return super.equals(o);
    }
}
