package ru.vldf.sportsportal.domain.sectional.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.AbstractRightsBasedEntity;
import ru.vldf.sportsportal.domain.sectional.booking.OrderEntity;
import ru.vldf.sportsportal.domain.sectional.booking.PlaygroundEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.PlayerEntity;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "user", schema = "common")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserEntity extends AbstractRightsBasedEntity {

    @Basic
    @Column(name = "email", nullable = false)
    private String email;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false)
    private String surname;

    @Basic
    @Column(name = "patronymic")
    private String patronymic;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "phone", nullable = false)
    private String phone;

    @Basic
    @Column(name = "confirm_code")
    private String confirmCode;

    @Basic
    @Column(name = "recover_code")
    private String recoverCode;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private PlayerEntity player;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "avatar_id",
            referencedColumnName = "id"
    )
    private PictureEntity avatar;


    @OneToMany(mappedBy = "customer")
    private Collection<OrderEntity> orders;

    @OneToMany(mappedBy = "uploader")
    private Collection<PictureEntity> pictures;

    @OneToMany(mappedBy = "mainCaptain")
    private Collection<TeamEntity> managedMainTeams;

    @OneToMany(mappedBy = "viceCaptain")
    private Collection<TeamEntity> managedViceTeams;


    @ManyToMany(mappedBy = "owners")
    private Collection<PlaygroundEntity> playgrounds;

    @ManyToMany
    @JoinTable(
            schema = "common",
            name = "authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<RoleEntity> roles;
}
