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
@Table(name = "player", schema = "tournament")
public class PlayerEntity extends AbstractVersionedEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerEntity)) return false;
        return super.equals(o);
    }
}
