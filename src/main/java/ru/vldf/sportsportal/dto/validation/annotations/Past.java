package ru.vldf.sportsportal.dto.validation.annotations;

import ru.vldf.sportsportal.dto.validation.validators.PastValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Namednev Artem
 */
@Documented
@Constraint(validatedBy = PastValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Past {

    String message() default "{sportsportal.validation.constraints.Past.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
