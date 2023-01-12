package bmstu.student.rubcov.lab2.hotelbookingsystem.gatewayapi.utils.validators.uuid;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Constraint(validatedBy={})
@Retention(RUNTIME)
@Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            flags={Pattern.Flag.CASE_INSENSITIVE},
            message = "Not a valid UUID")
public @interface UUID {

    String message() default "Not a valid UUID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
