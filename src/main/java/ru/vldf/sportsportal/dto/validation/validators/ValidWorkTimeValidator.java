package ru.vldf.sportsportal.dto.validation.validators;

import ru.vldf.sportsportal.dto.generic.specific.WorkTimeDTO;
import ru.vldf.sportsportal.dto.validation.annotations.ValidWorkTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidWorkTimeValidator implements ConstraintValidator<ValidWorkTime, WorkTimeDTO> {

    @Override
    public boolean isValid(WorkTimeDTO obj, ConstraintValidatorContext context) {
        return obj.getOpening().isBefore(obj.getClosing());
    }
}
