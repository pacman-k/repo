package by.epam.training.validator;

import by.epam.training.core.Bean;
import by.epam.training.security.CryptographyManager;
import by.epam.training.user.UserService;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;

import static by.epam.training.ApplicationConstants.*;

@Bean(name = "RegisterValidator")
@AllArgsConstructor
public class RegisterReqValidator implements ReqValidator {
    private static final String REGEX_EMAIL = "(^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$)";
    private static final String REGEX_PASSWORD = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}";
    private static final String REGEX_STRING = "[A-Za-zА-Яа-яЁё]{1,100}";

    UserService userService;
    CryptographyManager cryptographyManager;


    @Override
    public ValidationResult getValidationResult(HttpServletRequest request) {
        ValidationResult validationResult = new ValidationResult();

        final String login = request.getParameter(LOGIN_ATTRIBUTE);
        final String firstName = request.getParameter(FIRST_NAME_ATTRIBUTE);
        final String lastName = request.getParameter(LAST_NAME_ATTRIBUTE);
        final String email = request.getParameter(EMAIL_ATTRIBUTE);
        final String password = request.getParameter(PASSWORD_ATTRIBUTE);
        if (isEmpty(login) || isEmpty(firstName) || isEmpty(lastName)
                || isEmpty(email) || isEmpty(password)) {
            validationResult.addError("wrong input", "all fields should be initialized");
            return validationResult;
        }
        if (userService.isLoginTaken(login)) {
            validationResult.addError(LOGIN_ATTRIBUTE, "Login : " + login + " is taken");
        }
        if (!firstName.matches(REGEX_STRING)) {
            validationResult.addError(FIRST_NAME_ATTRIBUTE, "Invalid firstName format");
        }
        if (!lastName.matches(REGEX_STRING)) {
            validationResult.addError(LAST_NAME_ATTRIBUTE, "Invalid lastName format");
        }
        if (userService.isEmailTaken(email)) {
            validationResult.addError(EMAIL_ATTRIBUTE, "email is taken");
        }

        if (!password.matches(REGEX_PASSWORD)) {
            validationResult.addError(PASSWORD_ATTRIBUTE, "Invalid password format");
        }

        if (!email.matches(REGEX_EMAIL)) {
            validationResult.addError(EMAIL_ATTRIBUTE, "Invalid email format");
        }
        return validationResult;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }
}
