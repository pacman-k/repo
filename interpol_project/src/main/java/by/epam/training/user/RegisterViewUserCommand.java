package by.epam.training.user;


import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.epam.training.ApplicationConstants.REGISTER_VIEW_CMD_NAME;
import static by.epam.training.ApplicationConstants.VIEW_NAME_REQ_PARAMETER;

@Log4j
@Bean(name = REGISTER_VIEW_CMD_NAME)
public class RegisterViewUserCommand implements ServletCommand {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {

        try {
            req.setAttribute("viewName", REGISTER_VIEW_CMD_NAME);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong...", e);
            throw new CommandException("ERROR in RegisterViewUserCommand",e);
        }
    }
}
