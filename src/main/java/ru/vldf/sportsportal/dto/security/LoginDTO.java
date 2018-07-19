package ru.vldf.sportsportal.dto.security;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.net.URI;

public class LoginDTO implements DataTransferObject {

    private String login;
    private URI userURI;


    public String getLogin() {
        return login;
    }

    public LoginDTO setLogin(String login) {
        this.login = login;
        return this;
    }

    public URI getUserURI() {
        return userURI;
    }

    public LoginDTO setUserURI(URI userURI) {
        this.userURI = userURI;
        return this;
    }
}
