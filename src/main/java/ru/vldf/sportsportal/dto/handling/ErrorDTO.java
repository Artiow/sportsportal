package ru.vldf.sportsportal.dto.handling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Namednev Artem
 */
public class ErrorDTO implements DataTransferObject {

    @Getter
    private final UUID uuid;

    @Getter
    private final String exception;

    @Getter
    private final String message;

    @JsonProperty
    private final Cause cause;


    public ErrorDTO(UUID uuid, Throwable ex) {
        this.uuid = uuid;
        this.exception = ex.getClass().getName();
        this.message = ex.getMessage();
        this.cause = Optional
                .ofNullable(ex.getCause())
                .map(Cause::new)
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
                ? new Cause(causeClassName, causeMessage)
                : null;
    }


    @JsonIgnore
    public String getCauseException() {
        return cause.getException();
    }

    @JsonIgnore
    public String getCauseMessage() {
        return cause.getMessage();
    }


    private static class Cause {

        @Getter
        private final String exception;

        @Getter
        private final String message;


        public Cause(Throwable cause) {
            this.exception = cause.getClass().getName();
            this.message = cause.getMessage();
        }

        public Cause(String causeClassName, String causeMessage) {
            this.exception = causeClassName;
            this.message = causeMessage;
        }
    }
}
