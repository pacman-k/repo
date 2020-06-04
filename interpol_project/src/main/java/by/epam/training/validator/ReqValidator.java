package by.epam.training.validator;

import javax.servlet.http.HttpServletRequest;

public interface ReqValidator {
    ValidationResult getValidationResult(HttpServletRequest request);

}
