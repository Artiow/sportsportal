package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "team", schema = "tournament")
public class TeamEntity extends AbstractVersionedEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamEntity)) return false;
        return super.equals(o);
    }
}
