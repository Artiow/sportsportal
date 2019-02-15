package ru.vldf.sportsportal.dto.validation.validators;

import org.springframework.util.StringUtils;
import ru.vldf.sportsportal.dto.validation.annotations.NullOrNotBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Namednev Artem
 */
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) return true;
        return StringUtils.hasText(s);
    }
}
