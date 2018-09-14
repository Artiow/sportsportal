package ru.vldf.sportsportal.dto.validation.validators;

import ru.vldf.sportsportal.dto.generic.specific.WorkTimeDTO;
import ru.vldf.sportsportal.dto.validation.annotations.ValidWorkTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class ValidWorkTimeValidator implements ConstraintValidator<ValidWorkTime, WorkTimeDTO> {

    @Override
    public boolean isValid(WorkTimeDTO obj, ConstraintValidatorContext context) {
        LocalTime opening = obj.getOpening();
        LocalTime closing = obj.getClosing();
        int diff = Math.abs(closing.getMinute() - opening.getMinute());

        if ((diff % 30) != 0) return false;
        if ((diff != 0) && (!obj.getHalfHourAvailable())) return false;
        if (closing.equals(LocalTime.MIN)) return true;
        return opening.isBefore(closing);
    }
}
