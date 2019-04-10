package ru.vldf.sportsportal.dto.handling;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Namednev Artem
 */
public class ErrorMapDTO extends ErrorDTO {

    @Getter
    private final ImmutableList<String> objectErrorList;

    @Getter
    private final ImmutableMap<String, String> fieldErrorMap;


    public ErrorMapDTO(UUID uuid, Throwable ex, List<String> objectErrorList, Map<String, String> fieldErrorMap) {
        super(uuid, ex);
        this.objectErrorList = ofNullable(objectErrorList);
        this.fieldErrorMap = ofNullable(fieldErrorMap);
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, List<String> objectErrorList, Map<String, String> fieldErrorMap) {
        super(uuid, exceptionClassName, exceptionMessage);
        this.objectErrorList = ofNullable(objectErrorList);
        this.fieldErrorMap = ofNullable(fieldErrorMap);
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, String causeClassName, String causeMessage, List<String> objectErrorList, Map<String, String> fieldErrorMap) {
        super(uuid, exceptionClassName, exceptionMessage, causeClassName, causeMessage);
        this.objectErrorList = ofNullable(objectErrorList);
        this.fieldErrorMap = ofNullable(fieldErrorMap);
    }

    public ErrorMapDTO(UUID uuid, Throwable ex, Map<String, String> fieldErrorMap) {
        this(uuid, ex, null, fieldErrorMap);
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, Map<String, String> fieldErrorMap) {
        this(uuid, exceptionClassName, exceptionMessage, null, fieldErrorMap);
    }

    public ErrorMapDTO(UUID uuid, String exceptionClassName, String exceptionMessage, String causeClassName, String causeMessage, Map<String, String> fieldErrorMap) {
        this(uuid, exceptionClassName, exceptionMessage, causeClassName, causeMessage, null, fieldErrorMap);
    }


    private static <T> ImmutableList<T> ofNullable(List<T> list) {
        return Optional.ofNullable(list).map(ImmutableList::copyOf).orElse(ImmutableList.of());
    }

    private static <K, V> ImmutableMap<K, V> ofNullable(Map<K, V> map) {
        return Optional.ofNullable(map).map(ImmutableMap::copyOf).orElse(ImmutableMap.of());
    }
}
