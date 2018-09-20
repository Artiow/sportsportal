package ru.vldf.sportsportal.dto.handling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.util.Optional;
import java.util.UUID;

public class ErrorDTO implements DataTransferObject {

    private final UUID uuid;
    private final String exception;
    private final String message;

    @JsonProperty
    private final CauseDTO cause;


    public ErrorDTO(UUID uuid, Throwable ex) {
        this.uuid = uuid;
        this.exception = ex.getClass().getName();
        this.message = ex.getMessage();
        this.cause = Optional
                .ofNullable(ex.getCause())
                .map(CauseDTO::new)
                .orElse(null);
    }

    public ErrorDTO(UUID uuid, String exceptionClassName, String exceptionMessage) {
        this.uuid = uuid;
        this.exception = exceptionClassName;
        this.message = exceptionMessage;
        this.cause = null;
    }

    public ErrorDTO(UUID uuid, String exceptionClassName, String exceptionMessage, String causeClassName, String causeMessage) {
        this.uuid = uuid;
        this.exception = exceptionClassName;
        this.message = exceptionMessage;
        this.cause = ((causeClassName != null) && (causeMessage != null))
                ? new CauseDTO(causeClassName, causeMessage)
                : null;
    }


    public UUID getUuid() {
        return uuid;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    @JsonIgnore
    public String getCauseException() {
        return cause.getException();
    }

    @JsonIgnore
    public String getCauseMessage() {
        return cause.getMessage();
    }


    private static class CauseDTO {

        private final String exception;
        private final String message;


        public CauseDTO(Throwable cause) {
            this.exception = cause.getClass().getName();
            this.message = cause.getMessage();
        }

        public CauseDTO(String causeClassName, String causeMessage) {
            this.exception = causeClassName;
            this.message = causeMessage;
        }


        public String getException() {
            return exception;
        }

        public String getMessage() {
            return message;
        }
    }
}
