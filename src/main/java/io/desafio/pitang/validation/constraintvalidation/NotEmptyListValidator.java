package io.desafio.pitang.validation.constraintvalidation;

import io.desafio.pitang.validation.NotEmptyList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyListValidator
        implements ConstraintValidator<NotEmptyList, List> {

    @Override
    public boolean isValid(List list,
                           ConstraintValidatorContext constraintValidatorContext) {
        return list != null && !list.isEmpty();
    }

    @Override
    public void initialize( NotEmptyList constraintAnnotation ) {
    }
}
