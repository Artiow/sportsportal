package ru.vldf.sportsportal.domain.sectional.tournament;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "team", schema = "tournament")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class TeamEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "locked", nullable = false)
    private Boolean isLocked = false;

    @Basic
    @Column(name = "disabled", nullable = false)
    private Boolean isDisabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_id", referencedColumnName = "id", nullable = false)
    private UserEntity mainCaptain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vice_captain_id", referencedColumnName = "id", nullable = false)
    private UserEntity viceCaptain;
}
