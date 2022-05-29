package ru.yandex.practicum.filmorate.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PositiveDurationValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface PositiveDuration {
	String message() default "Duration must be positive";
	Class <?> [] groups() default {};
	Class <? extends Payload> [] payload() default {};
}
