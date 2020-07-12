package com.dan.travel_agent.service;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CheckIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CheckId {

    String message() default "id must be specified";
    boolean checkIfExist() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
