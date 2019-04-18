package ru.vldf.sportsportal.domain.general;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractAuditableEntity extends AbstractRightsBasedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    private UserEntity creator;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id", referencedColumnName = "id", nullable = false)
    private UserEntity updater;

    @Basic
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
}
