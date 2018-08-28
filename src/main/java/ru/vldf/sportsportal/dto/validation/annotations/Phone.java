package ru.vldf.sportsportal.dto.validation.annotations;

import ru.vldf.sportsportal.dto.validation.validators.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {

    String message() default "{sportsportal.validation.constraints.Phone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
