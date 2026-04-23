package lt.teamProject.smartCarCosts.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FullNameValidator implements ConstraintValidator<ValidFullName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        // Check for null
        if (value == null || value.isBlank()) {
            context.buildConstraintViolationWithTemplate("Full name is required")
                    .addConstraintViolation();
            return false;
        }
        // Checking the length
        if (value.length() < 2 || value.length() > 30) {
            context.buildConstraintViolationWithTemplate("Full name must be between 2 and 30 characters")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
