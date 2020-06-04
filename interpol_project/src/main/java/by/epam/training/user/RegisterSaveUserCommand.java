package by.epam.training.user;

import by.epam.training.ApplicationConstants;
import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.security.CryptographyManager;
import by.epam.training.validator.RegisterReqValidator;
import by.epam.training.validator.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.epam.training.ApplicationConstants.*;

@Log4j
@Bean(name = REGISTER_SAVE_CMD_NAME)
@AllArgsConstructor
public class RegisterSaveUserCommand implements ServletCommand {

    private UserService userService;
    private CryptographyManager cryptographyManager;
    private RegisterReqValidator registerReqValidator;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            ValidationResult result = registerReqValidator.getValidationResult(req);
            UserDto dto = parseUserToDto(req);
            if (result.isValid()) {
                userService.registerUser(dto);
                req.setAttribute(LOGIN_ATTRIBUTE, dto.getLogin());
                req.setAttribute(PASSWORD_ATTRIBUTE, cryptographyManager.decode(dto.getPassword()));
                req.getRequestDispatcher("/?commandName=" + ApplicationConstants.LOGIN_CMD_NAME).forward(req, resp);
            } else {
                req.setAttribute("user", dto);
                req.setAttribute("error", result.getAllVales());
                req.setAttribute("viewName", REGISTER_VIEW_CMD_NAME);
                req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
            }
        } catch (IllegalStateException | ServletException | IOException e) {
            log.error("Failed register user");
            throw new CommandException("Register command", e);
        }
    }

    private UserDto parseUserToDto(HttpServletRequest req) {
        final String login = req.getParameter(LOGIN_ATTRIBUTE);
        final String firstName = req.getParameter(FIRST_NAME_ATTRIBUTE);
        final String lastName = req.getParameter(LAST_NAME_ATTRIBUTE);
        final String email = req.getParameter(EMAIL_ATTRIBUTE);
        final String password = req.getParameter(PASSWORD_ATTRIBUTE);
        UserDto dto = new UserDto();
        dto.setPassword(password != null ? cryptographyManager.encode(password) : null);
        dto.setLogin(login);
        dto.setEmail(email);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        return dto;
    }
}

