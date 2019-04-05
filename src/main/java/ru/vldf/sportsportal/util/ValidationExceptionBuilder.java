package ru.vldf.sportsportal.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

/**
 * @author Namednev Artem
 */
public final class ValidationExceptionBuilder {

    public static MethodArgumentNotValidException buildFor(MethodParameter parameter, String objectName, Map<String, String> errorMap) {
        return new MethodArgumentNotValidException(parameter, resultFor(objectName, errorMap));
    }

    private static AbstractBindingResult resultFor(String objectName, Map<String, String> errorMap) {
        AbstractBindingResult result = new BeanPropertyBindingResult(null, objectName);
        for (Map.Entry<String, String> entry : errorMap.entrySet()) {
            String code = entry.getKey();
            String message = entry.getValue();
            result.reject(code, new DefaultMessageSourceResolvable[]{new DefaultMessageSourceResolvable(new String[]{code}, message)}, message);
        }
        return result;
    }
}
