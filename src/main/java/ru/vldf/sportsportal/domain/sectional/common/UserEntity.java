package ru.vldf.sportsportal.domain.sectional.common;

import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user", schema = "common")
public class UserEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "login", nullable = false)
    private String login;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "email", nullable = false)
    private String email;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false)
    private String surname;

    @Basic
    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @Basic
    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private PictureEntity avatar;

    @OneToMany(mappedBy = "customer")
    private Collection<OrderEntity> orders;

    @ManyToMany
    @JoinTable(
            schema = "common",
            name = "authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<RoleEntity> roles;

    @ManyToMany(mappedBy = "owners")
    private Collection<PlaygroundEntity> playgrounds;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PictureEntity getAvatar() {
        return avatar;
    }

    public void setAvatar(PictureEntity avatar) {
        this.avatar = avatar;
    }

    public Collection<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(Collection<OrderEntity> orders) {
        this.orders = orders;
    }

    public Collection<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleEntity> roles) {
        this.roles = roles;
    }

    public Collection<PlaygroundEntity> getPlaygrounds() {
        return playgrounds;
    }

    public void setPlaygrounds(Collection<PlaygroundEntity> playgrounds) {
        this.playgrounds = playgrounds;
    }
}
