package ru.vldf.sportsportal.dto.security;

import java.util.UUID;

public class JwtPayload {

    private Integer userId;
    private Integer keyId;
    private UUID uuid;


    public Integer getUserId() {
        return userId;
    }

    public JwtPayload setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public JwtPayload setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public JwtPayload setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }
}
