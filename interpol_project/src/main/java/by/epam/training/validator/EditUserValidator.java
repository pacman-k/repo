package by.epam.training.validator;

import by.epam.training.core.Bean;
import by.epam.training.user.UserService;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;

import static by.epam.training.ApplicationConstants.*;

@Bean(name = "EditUserValidator")
@AllArgsConstructor
public class EditUserValidator implements ReqValidator {
    private static final String REGEX_EMAIL = "(^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$)";
    private static final String REGEX_STRING = "[A-Za-zА-Яа-яЁё]{1,100}";

    UserService userService;
    @Override
    public ValidationResult getValidationResult(HttpServletRequest request) {
        ValidationResult validationResult = new ValidationResult();
        final String id = request.getParameter("id");
        final String firstName = request.getParameter(FIRST_NAME_ATTRIBUTE);
        final String lastName = request.getParameter(LAST_NAME_ATTRIBUTE);
        final String email = request.getParameter(EMAIL_ATTRIBUTE);

        if (isEmpty(id) || isEmpty(firstName) || isEmpty(lastName)
                || isEmpty(email)) {
            validationResult.addError("wrong input", "all fields should be initialized");
            return validationResult;
        }

        Long userId = Long.parseLong(id);
        Long currentId = Long.parseLong(request.getSession().getAttribute("userId").toString());
        if (!userId.equals(currentId)){
            validationResult.addError("role", "you dont have access to this command");
            return validationResult;
        }

        if (!firstName.matches(REGEX_STRING)) {
            validationResult.addError(FIRST_NAME_ATTRIBUTE, "Invalid firstName format");
        }
        if (!lastName.matches(REGEX_STRING)) {
            validationResult.addError(LAST_NAME_ATTRIBUTE, "Invalid lastName format");
        }
        if (!email.matches(REGEX_EMAIL)) {
            validationResult.addError(EMAIL_ATTRIBUTE, "Invalid email format");
        }
        if (userService.isEmailTaken(email)) {
            validationResult.addError(EMAIL_ATTRIBUTE, "email is taken");
        }
        return validationResult;
    }
    private static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }
}
