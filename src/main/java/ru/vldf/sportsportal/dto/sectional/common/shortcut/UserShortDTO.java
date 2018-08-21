package ru.vldf.sportsportal.dto.sectional.common.shortcut;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import java.net.URI;
import java.util.List;

public class UserShortDTO extends AbstractIdentifiedDTO {

    private Integer id;
    private String login;
    private String name;
    private String surname;
    private String patronymic;
    private String address;
    private String phone;
    private URI userURL;
    private URI avatarURL;
    private List<String> roles;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public UserShortDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public UserShortDTO setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserShortDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public UserShortDTO setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public UserShortDTO setPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserShortDTO setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserShortDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public URI getUserURL() {
        return userURL;
    }

    public UserShortDTO setUserURL(URI userURL) {
        this.userURL = userURL;
        return this;
    }

    public URI getAvatarURL() {
        return avatarURL;
    }

    public UserShortDTO setAvatarURL(URI avatarURL) {
        this.avatarURL = avatarURL;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UserShortDTO setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
