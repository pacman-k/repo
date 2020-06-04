package by.epam.training.user;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.validator.EditUserValidator;
import by.epam.training.validator.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.epam.training.ApplicationConstants.*;

@Log4j
@Bean(name = EDIT_USER_CMD)
@AllArgsConstructor
public class EditUserCommand implements ServletCommand {
    UserService userService;
    EditUserValidator editUserValidator;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        ValidationResult validationResult = editUserValidator.getValidationResult(req);
        try {
            if (validationResult.isValid()) {
                Long id = Long.parseLong(req.getParameter("id"));
                UserDto userDto = userService.findById(id).orElseThrow(() -> new ServletException("cant get user:" + id));
                userDto.setFirstName(req.getParameter(FIRST_NAME_ATTRIBUTE));
                userDto.setLastName(req.getParameter(LAST_NAME_ATTRIBUTE));
                userDto.setEmail(req.getParameter(EMAIL_ATTRIBUTE));
                if (!userService.updateUser(userDto)) {
                    req.setAttribute("error", "cant update this user");
                    req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
                }
                req.getRequestDispatcher("/?commandName=" + PROFILE_VIEW_CMD_NAME).forward(req, resp);
            } else {
                req.setAttribute("error", validationResult.getAllVales());
                req.setAttribute("oldUser", parseUserToDto(req));
                req.setAttribute("viewName", EDIT_USER_VIEW_CMD);
                req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
            }
        } catch (ServletException | IOException e) {
            log.error("cant edit user cause :" + e);
            throw new CommandException("cant update user", e);
        }

    }

    private UserDto parseUserToDto(HttpServletRequest req) {
        final String firstName = req.getParameter(FIRST_NAME_ATTRIBUTE);
        final String lastName = req.getParameter(LAST_NAME_ATTRIBUTE);
        final String email = req.getParameter(EMAIL_ATTRIBUTE);
        return UserDto.builder().firstName(firstName)
                .lastName(lastName).email(email).build();
    }
}
