package ru.vldf.sportsportal.service.security.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author Namednev Artem
 */
public enum ExpirationType {

    ACCESS,
    REFRESH;

    @Getter
    @JsonValue
    private final String value;

    ExpirationType() {
        value = name().toLowerCase();
    }
}
