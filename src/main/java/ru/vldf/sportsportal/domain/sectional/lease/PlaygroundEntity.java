package ru.vldf.sportsportal.domain.sectional.lease;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "playground", schema = "lease")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class PlaygroundEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @Basic
    @Column(name = "phone", nullable = false)
    private String phone;

    @Basic
    @Column(name = "rate", nullable = false)
    private Integer rate;

    @Basic
    @Column(name = "opening", nullable = false)
    private Timestamp opening = Timestamp.valueOf(JavaTimeMapper.MIN);

    @Basic
    @Column(name = "closing", nullable = false)
    private Timestamp closing = Timestamp.valueOf(JavaTimeMapper.MIN);

    @Basic
    @Column(name = "half_hour_available", nullable = false)
    private Boolean halfHourAvailable = false;

    @Basic
    @Column(name = "full_hour_required", nullable = false)
    private Boolean fullHourRequired = false;

    @Basic
    @Column(name = "price", nullable = false)
    private BigDecimal price = BigDecimal.valueOf(0, 2);

    @Basic
    @Column(name = "test", nullable = false)
    private Boolean test = true;


    @OrderBy("pk.datetime")
    @OneToMany(mappedBy = "pk.playground")
    private Collection<ReservationEntity> reservations;

    @ManyToMany
    @JoinTable(
            schema = "lease",
            name = "playground_specialization",
            joinColumns = @JoinColumn(name = "playground_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "sport_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<SportEntity> specializations;

    @ManyToMany
    @JoinTable(
            schema = "lease",
            name = "playground_capability",
            joinColumns = @JoinColumn(name = "playground_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "feature_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<FeatureEntity> capabilities;

    @ManyToMany
    @JoinTable(
            schema = "lease",
            name = "playground_ownership",
            joinColumns = @JoinColumn(name = "playground_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<UserEntity> owners;

    @ManyToMany
    @JoinTable(
            schema = "lease",
            name = "playground_photo",
            joinColumns = @JoinColumn(name = "playground_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "picture_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<PictureEntity> photos;
}
