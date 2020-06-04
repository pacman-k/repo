package by.epam.training.validator;

import by.epam.training.core.Bean;

import javax.servlet.http.HttpServletRequest;


import java.util.Map;

import static by.epam.training.ApplicationConstants.*;

@Bean(name = "ClaimCreateValidator")
public class CreateClaimValidator implements ReqValidator {
    @Override
    public ValidationResult getValidationResult(HttpServletRequest request) {
        ValidationResult validationResult = new ValidationResult();
        Map<String, Object> reqMap = (Map<String, Object>) request.getAttribute("requestMap");
       if (reqMap == null || reqMap.isEmpty()){
           validationResult.addError("map from req", "cant get map of parameters from request");
           return validationResult;
       }
        final String firstName = reqMap.containsKey(FIRST_NAME_ATTRIBUTE) ? reqMap.get(FIRST_NAME_ATTRIBUTE).toString() : null;
        final String lastName = reqMap.containsKey(LAST_NAME_ATTRIBUTE) ? reqMap.get(FIRST_NAME_ATTRIBUTE).toString() : null;
        final String birthday = reqMap.containsKey(BIRTHDAY_ATTRIBUTE) ? reqMap.get(FIRST_NAME_ATTRIBUTE).toString() : null;
        final String description = reqMap.containsKey(DESCRIPTION_ATTRIBUTE) ? reqMap.get(DESCRIPTION_ATTRIBUTE).toString() : null;
        final String country = reqMap.containsKey(COUNTRY_ATTRIBUTE) ? reqMap.get(COUNTRY_ATTRIBUTE).toString() : null;
        final String date = reqMap.containsKey(BIRTHDAY_ATTRIBUTE)? reqMap.get(BIRTHDAY_ATTRIBUTE).toString() : null;


        if (isEmpty(firstName) || isEmpty(description) || isEmpty(country) || isEmpty(date) || isEmpty(lastName) || isEmpty(birthday)) {
            validationResult.addError("wrong input", "firstname, host country, birthday and description are required");
        }
        return validationResult;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }
}
