package ru.vldf.sportsportal.dto.security;

public class JwtPair {

    private String accessToken;
    private String refreshToken;


    public String getAccessToken() {
        return accessToken;
    }

    public JwtPair setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public JwtPair setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
