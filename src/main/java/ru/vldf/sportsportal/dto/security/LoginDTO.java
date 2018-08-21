package ru.vldf.sportsportal.dto.security;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.net.URI;
import java.util.List;

public class LoginDTO implements DataTransferObject {

    private String login;
    private URI userURL;
    private URI avatarURL;
    private List<String> roles;


    public String getLogin() {
        return login;
    }

    public LoginDTO setLogin(String login) {
        this.login = login;
        return this;
    }

    public URI getUserURL() {
        return userURL;
    }

    public LoginDTO setUserURL(URI userURL) {
        this.userURL = userURL;
        return this;
    }

    public URI getAvatarURL() {
        return avatarURL;
    }

    public LoginDTO setAvatarURL(URI avatarURL) {
        this.avatarURL = avatarURL;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public LoginDTO setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
