package by.epam.training.user;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static by.epam.training.ApplicationConstants.VIEW_ALL_USERS_CMD_NAME;
import static by.epam.training.ApplicationConstants.CMD_REQ_PARAMETER;

@Log4j
@Bean(name = VIEW_ALL_USERS_CMD_NAME)
@AllArgsConstructor
@Getter
public class UserListViewCommand implements ServletCommand {
    private UserService userService;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        final List<UserDto> allUsers = userService.getAllUsers();
        req.setAttribute("users", allUsers);
        try {
            req.setAttribute("viewName", VIEW_ALL_USERS_CMD_NAME);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong...", e);
            throw new CommandException("cant get users list",e);
        }
    }
}
