package ru.vldf.sportsportal.util;

import org.springframework.core.MethodParameter;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

/**
 * @author Namednev Artem
 */
public final class ValidationExceptionBuilder {

    public static MethodArgumentNotValidException buildFor(MethodParameter parameter, Object objectTarget, String objectName, Map<String, String> errorMap) {
        return new MethodArgumentNotValidException(parameter, resultFor(objectTarget, objectName, errorMap));
    }

    private static AbstractBindingResult resultFor(Object objectTarget, String objectName, Map<String, String> errorMap) {
        AbstractBindingResult result = new BeanPropertyBindingResult(objectTarget, objectName);
        errorMap.forEach((field, message) -> result.rejectValue(field, "", message));
        return result;
    }
}
