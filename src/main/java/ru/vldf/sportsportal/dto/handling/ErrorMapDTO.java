package ru.vldf.sportsportal.dto.handling;

import lombok.Getter;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

/**
 * @author Namednev Artem
 */
public class ErrorMapDTO extends ErrorDTO {

    @Getter
    private final MultiValueMap<String, String> errors;


    public ErrorMapDTO(UUID uuid, Throwable ex, MultiValueMap<String, String> errors) {
        super(uuid, ex);
        this.errors = errors;
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, MultiValueMap<String, String> errors) {
        super(uuid, exceptionClassName, exceptionMessage);
        this.errors = errors;
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, String causeClassName, String causeMessage, MultiValueMap<String, String> errors) {
        super(uuid, exceptionClassName, exceptionMessage, causeClassName, causeMessage);
        this.errors = errors;
    }
}
