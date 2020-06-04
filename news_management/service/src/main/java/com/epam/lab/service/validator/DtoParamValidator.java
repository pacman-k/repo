package com.epam.lab.service.validator;

import com.epam.lab.dto.Dto;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Component
public class DtoParamValidator implements ConstraintValidator<ValidDtoParam, Dto> {
    private String[] params;
    private String message;

    @Override
    public void initialize(ValidDtoParam constraintAnnotation) {
        params = constraintAnnotation.paramNames();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Dto dto, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        if (Objects.isNull(dto)) return false;
        for (String param : params) {
            try {
                String value = BeanUtils.getProperty(dto, param);
                if (isEmpty(value)) return false;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }
}
