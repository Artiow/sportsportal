package ru.vldf.sportsportal.dto.security;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

public class TokenDTO implements DataTransferObject {

    private String tokenHash;
    private String tokenType;
    private LoginDTO userInfo;


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

    public LoginDTO getUserInfo() {
        return userInfo;
    }

    public TokenDTO setUserInfo(LoginDTO userInfo) {
        this.userInfo = userInfo;
        return this;
    }
}
