package ru.vldf.sportsportal.dto.security;

public class JwtPairDTO {

    private String accessToken;
    private String refreshToken;


    public String getAccessToken() {
        return accessToken;
    }

    public JwtPairDTO setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public JwtPairDTO setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
