package ru.vldf.sportsportal.dto.security;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

public class TokenDTO implements DataTransferObject {

    private String accessToken;
    private String tokenType;
    private LoginDTO user;


    public String getAccessToken() {
        return accessToken;
    }

    public TokenDTO setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public TokenDTO setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public LoginDTO getUser() {
        return user;
    }

    public TokenDTO setUser(LoginDTO user) {
        this.user = user;
        return this;
    }
}
