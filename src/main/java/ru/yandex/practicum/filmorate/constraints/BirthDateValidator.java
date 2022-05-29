package ru.yandex.practicum.filmorate.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {
	@Override
	public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
		return birthDate.isBefore(LocalDate.now());
	}
}
