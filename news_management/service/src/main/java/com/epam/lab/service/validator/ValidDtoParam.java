package com.epam.lab.service.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DtoParamValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ValidDtoParam {
    String[] paramNames() default {};
    String message() default "{not enough initialised parameters}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
