package by.epam.training.user;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.epam.training.ApplicationConstants.LOGIN_VIEW_CMD_NAME;

@Log4j
@Bean(name = LOGIN_VIEW_CMD_NAME)
public class LoginUserViewCommand implements ServletCommand {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            req.setAttribute("viewName", LOGIN_VIEW_CMD_NAME);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong...", e);
            throw new CommandException("ERROR in LoginUserViewCommand",e);
        }
    }
}
