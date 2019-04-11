package ru.vldf.sportsportal.dto.validation.annotations;

import ru.vldf.sportsportal.dto.validation.validators.ValidWorkTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Namednev Artem
 */
@Documented
@Constraint(validatedBy = ValidWorkTimeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidWorkTime {

    String[] fields() default {"opening", "closing", "halfHourAvailable", "fullHourRequired"};

    String message() default "{sportsportal.validation.constraints.ValidWorkTime.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
