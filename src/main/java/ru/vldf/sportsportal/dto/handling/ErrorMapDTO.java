package ru.vldf.sportsportal.dto.handling;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Namednev Artem
 */
public class ErrorMapDTO extends ErrorDTO {

    private final Map<String, String> errors;


    public ErrorMapDTO(UUID uuid, Throwable ex, Map<String, String> errors) {
        super(uuid, ex);
        this.errors = Optional
                .ofNullable(errors)
                .map(HashMap::new)
                .orElse(new HashMap<>());
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, Map<String, String> errors) {
        super(uuid, exceptionClassName, exceptionMessage);
        this.errors = Optional
                .ofNullable(errors)
                .map(HashMap::new)
                .orElse(new HashMap<>());
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, String causeClassName, String causeMessage, Map<String, String> errors) {
        super(uuid, exceptionClassName, exceptionMessage, causeClassName, causeMessage);
        this.errors = Optional
                .ofNullable(errors)
                .map(HashMap::new)
                .orElse(new HashMap<>());
    }


    public Map<String, String> getErrors() {
        return ImmutableMap.copyOf(errors);
    }
}
