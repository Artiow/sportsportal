package ru.vldf.sportsportal.dto.validation.annotations;

import ru.vldf.sportsportal.dto.validation.validators.ValidLocationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Namednev Artem
 */
@Documented
@Constraint(validatedBy = ValidLocationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocation {

    String[] fields() default {"locationLatitude", "locationLongitude"};

    String message() default "{sportsportal.validation.constraints.ValidLocation.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
