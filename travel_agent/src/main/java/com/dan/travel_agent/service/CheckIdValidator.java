package com.dan.travel_agent.service;

import com.dan.travel_agent.models.City;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckIdValidator implements ConstraintValidator<CheckId, City> {
    private static final String EXISTS_ERROR = "city with such id is not exist";

    @PersistenceContext
    private EntityManager entityManager;

    private String message;
    private boolean checkIfExist;

    @Override
    public void initialize(CheckId constraintAnnotation) {
        message = constraintAnnotation.message();
        checkIfExist = constraintAnnotation.checkIfExist();
    }

    @Override
    public boolean isValid(City value, ConstraintValidatorContext context) {
        boolean idNotNull = value.getCityId() != null;
        if (idNotNull && checkIfExist) {
            setExceptionMessage(context, EXISTS_ERROR);
            return isExist(value.getCityId());
        }
        setExceptionMessage(context, message);
        return idNotNull;
    }

    private boolean isExist(Long id) {
        return entityManager == null || entityManager.find(City.class, id) != null;
    }

    private static void setExceptionMessage(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
    }
}
