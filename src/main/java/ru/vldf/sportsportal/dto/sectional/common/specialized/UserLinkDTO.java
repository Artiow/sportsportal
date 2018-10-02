package ru.vldf.sportsportal.dto.sectional.common.specialized;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class UserLinkDTO extends AbstractIdentifiedDTO {

    @NotNull
    private Integer id;

    private String login;
    private String email;
    private String name;
    private String surname;
    private String patronymic;
    private URI userURL;
    private URI avatarURL;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public UserLinkDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public UserLinkDTO setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserLinkDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserLinkDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public UserLinkDTO setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public UserLinkDTO setPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public URI getUserURL() {
        return userURL;
    }

    public UserLinkDTO setUserURL(URI userURL) {
        this.userURL = userURL;
        return this;
    }

    public URI getAvatarURL() {
        return avatarURL;
    }

    public UserLinkDTO setAvatarURL(URI avatarURL) {
        this.avatarURL = avatarURL;
        return this;
    }
}
