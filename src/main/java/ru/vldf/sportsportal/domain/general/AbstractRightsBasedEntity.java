package ru.vldf.sportsportal.domain.general;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractRightsBasedEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "locked", nullable = false)
    private Boolean isLocked = false;

    @Basic
    @Column(name = "disabled", nullable = false)
    private Boolean isDisabled = true;
}
