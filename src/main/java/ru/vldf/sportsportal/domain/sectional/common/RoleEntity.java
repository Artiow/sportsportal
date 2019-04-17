package ru.vldf.sportsportal.domain.sectional.common;

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
@Table(name = "role", schema = "common")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class RoleEntity extends AbstractDictionaryEntity {

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;
}
