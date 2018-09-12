package ru.vldf.sportsportal.dto.validation.validators;

import ru.vldf.sportsportal.dto.validation.annotations.Login;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<Login, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        return ((s != null) && s.matches("^(?![0-9._])(?!.*[_.]{2})[a-z0-9._]+(?<![_.])$"));
    }
}
