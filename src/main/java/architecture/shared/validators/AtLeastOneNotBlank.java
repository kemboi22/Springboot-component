package architecture.shared.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

@Constraint(validatedBy = AtLeastOneNotBlank.Validator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AtLeastOneNotBlank {
    String[] fields();

    String message() default "{validators.atLeastOneNotBlank}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    final class Validator implements ConstraintValidator<AtLeastOneNotBlank, Object> {
        private String[] fields;

        @Override
        public void initialize(final AtLeastOneNotBlank annotation) {
            fields = annotation.fields();
        }

        public boolean isValid(final Object instance, final ConstraintValidatorContext context) {
            for (final var field : fields) {
                if (!ObjectUtils.isEmpty(new BeanWrapperImpl(instance).getPropertyValue(field))) return true;
            }

            return false;
        }
    }
}
