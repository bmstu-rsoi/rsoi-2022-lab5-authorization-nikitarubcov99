package bmstu.student.rubcov.lab2.hotelbookingsystem.gatewayapi.utils.validators.uuid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDValidator implements ConstraintValidator<UUID, String> {

    public boolean isValid(String uuid, ConstraintValidatorContext cxt) {

        Pattern pattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(uuid);
        return matcher.matches();

    }

}
