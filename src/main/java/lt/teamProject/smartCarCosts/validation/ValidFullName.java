package lt.teamProject.smartCarCosts.validation;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FullNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFullName {

    String message() default "Invalid full name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
