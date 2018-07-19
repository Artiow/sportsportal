package ru.vldf.sportsportal.dto.handling;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ErrorMapDTO extends ErrorDTO {

    private final Map<String, String> errors;


    public ErrorMapDTO(UUID uuid, String exception, String message, Map<String, String> errors) {
        super(uuid, exception, message);
        this.errors = new HashMap<>(errors);
    }


    public Map<String, String> getErrors() {
        return errors;
    }
}
