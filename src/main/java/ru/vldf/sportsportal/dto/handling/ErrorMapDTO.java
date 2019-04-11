package ru.vldf.sportsportal.dto.handling;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Namednev Artem
 */
public class ErrorMapDTO extends ErrorDTO {

    @Getter
    private final ImmutableMap<String, String> errors;


    public ErrorMapDTO(UUID uuid, Throwable ex, Map<String, String> errors) {
        super(uuid, ex);
        this.errors = ofNullable(errors);
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, Map<String, String> errors) {
        super(uuid, exceptionClassName, exceptionMessage);
        this.errors = ofNullable(errors);
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, String causeClassName, String causeMessage, Map<String, String> errors) {
        super(uuid, exceptionClassName, exceptionMessage, causeClassName, causeMessage);
        this.errors = ofNullable(errors);
    }


    private static <K, V> ImmutableMap<K, V> ofNullable(Map<K, V> map) {
        return Optional.ofNullable(map).map(ImmutableMap::copyOf).orElse(ImmutableMap.of());
    }
}
