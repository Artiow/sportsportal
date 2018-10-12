package ru.vldf.sportsportal.dto.sectional.common.specialized;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class UserLinkDTO extends AbstractIdentifiedDTO {

    @NotNull
    private Integer id;

    private String email;
    private String username;
    private String phone;
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

    public String getEmail() {
        return email;
    }

    public UserLinkDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserLinkDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserLinkDTO setPhone(String phone) {
        this.phone = phone;
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
