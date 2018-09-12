package ru.vldf.sportsportal.dto.validation.validators;

import ru.vldf.sportsportal.dto.validation.annotations.Future;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FutureValidator implements ConstraintValidator<Future, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime ldt, ConstraintValidatorContext constraintValidatorContext) {
        return ((ldt != null) && ldt.isAfter(LocalDateTime.now()));
    }
}
