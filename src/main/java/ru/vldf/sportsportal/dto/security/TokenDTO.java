package ru.vldf.sportsportal.dto.security;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

public class TokenDTO implements DataTransferObject {

    private String tokenHash;
    private String tokenType;
    private LoginDTO login;


    public String getTokenHash() {
        return tokenHash;
    }

    public TokenDTO setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public TokenDTO setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public LoginDTO getLogin() {
        return login;
    }

    public TokenDTO setLogin(LoginDTO login) {
        this.login = login;
        return this;
    }
}
