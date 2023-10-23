package lost.test.quarkus.common;


import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD
})
@Constraint(validatedBy = RangeLowerBound.Validator.class)
public @interface RangeLowerBound {

    String message() default "range must have lower bound";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    class Validator implements ConstraintValidator<RangeLowerBound, Range<?>> {

        @Override
        public boolean isValid(Range<?> range,
                               ConstraintValidatorContext constraintValidatorContext) {
            if (range == null) return false;
            return range.hasLowerBound();
        }
    }
}
