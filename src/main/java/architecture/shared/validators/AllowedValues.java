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
import java.util.Arrays;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

@Constraint(validatedBy = AllowedValues.Validator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AllowedValues {
    String field();

    String[] values();

    String message() default "{validators.allowedValues}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    final class Validator implements ConstraintValidator<AllowedValues, Object> {
        private String field;
        private String[] values;

        @Override
        public void initialize(final AllowedValues annotation) {
            field = annotation.field();
            values = annotation.values();
        }

        public boolean isValid(final Object instance, final ConstraintValidatorContext context) {
            final var value = new BeanWrapperImpl(instance).getPropertyValue(field);

            return ObjectUtils.isEmpty(value) || Arrays.asList(values).contains(value.toString());
        }
    }
}
