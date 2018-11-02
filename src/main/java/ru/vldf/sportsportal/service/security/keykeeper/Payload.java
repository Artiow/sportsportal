package ru.vldf.sportsportal.service.security.keykeeper;

import java.util.UUID;

public class Payload {

    private Integer userId;
    private Integer keyId;
    private UUID uuid;


    public Integer getUserId() {
        return userId;
    }

    public Payload setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public Payload setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Payload setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }
}
