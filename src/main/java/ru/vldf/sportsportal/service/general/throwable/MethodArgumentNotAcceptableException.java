package ru.vldf.sportsportal.service.general.throwable;

import org.springframework.core.MethodParameter;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

/**
 * @author Artem Namednev
 */
public class MethodArgumentNotAcceptableException extends MethodArgumentNotValidException {

    /**
     * Constructor for {@link MethodArgumentNotAcceptableException}.
     *
     * @param parameter     the parameter that failed validation.
     * @param bindingResult the results of the validation.
     */
    public MethodArgumentNotAcceptableException(MethodParameter parameter, BindingResult bindingResult) {
        super(parameter, bindingResult);
    }


    /**
     * Returns new exception instance for manual validation.
     *
     * @param parameter the parameter that failed validation.
     * @param target    the target bean to bind onto.
     * @param errors    the error map.
     * @return exception.
     */
    public static MethodArgumentNotAcceptableException by(MethodParameter parameter, Object target, Map<String, String> errors) {
        return new MethodArgumentNotAcceptableException(parameter, resultFor(target, parameter.getParameter().getName(), errors));
    }

    private static AbstractBindingResult resultFor(Object objectTarget, String objectName, Map<String, String> errorMap) {
        AbstractBindingResult result = new BeanPropertyBindingResult(objectTarget, objectName);
        errorMap.forEach((field, message) -> result.rejectValue(field, "", message));
        return result;
    }
}
