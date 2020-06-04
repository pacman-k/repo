package by.epam.training.user;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.epam.training.ApplicationConstants.PROFILE_VIEW_CMD_NAME;

@Log4j
@Bean(name = PROFILE_VIEW_CMD_NAME)
@AllArgsConstructor
public class ProfileUserViewCommand implements ServletCommand {
    UserService userService;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        final UserDto user = userService.findById(userId).orElseThrow(() -> new CommandException("cant find user by such id" + userId));
        req.setAttribute("user", user);
        try {
            req.setAttribute("viewName", PROFILE_VIEW_CMD_NAME);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong in profileUserView");
            throw new CommandException("ERROR in RegisterViewUserCommand", e);
        }
    }
}
