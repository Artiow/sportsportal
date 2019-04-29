package ru.vldf.sportsportal.dto.validation.validators;

import ru.vldf.sportsportal.dto.validation.annotations.Trimmed;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Artem Namednev
 */
public class TrimmedValidator implements ConstraintValidator<Trimmed, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) return true;
        char[] arr = s.toCharArray();
        return (arr.length == 0) || ((arr[0] > ' ') && (arr[arr.length - 1] > ' '));
    }
}
