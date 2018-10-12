package ru.vldf.sportsportal.dto.security;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import java.net.URI;
import java.util.List;

public class LoginDTO extends AbstractIdentifiedDTO {

    private Integer id;
    private String email;
    private String username;
    private URI userURL;
    private URI avatarURL;
    private List<String> roles;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public LoginDTO setId(Integer id) {
        this.id = id;
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
