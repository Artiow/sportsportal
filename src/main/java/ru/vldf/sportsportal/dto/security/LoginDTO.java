package ru.vldf.sportsportal.dto.security;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.net.URI;
import java.util.List;

public class LoginDTO implements DataTransferObject {

    private String login;
    private String email;
    private String username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
