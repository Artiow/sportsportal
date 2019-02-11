package ru.vldf.sportsportal.dto.validation.validators;

import ru.vldf.sportsportal.dto.validation.annotations.Future;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 * @author Namednev Artem
 */
public class FutureDateValidator implements ConstraintValidator<Future, Date> {

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) return true;
        return date.getTime() > System.currentTimeMillis();
    }
}
