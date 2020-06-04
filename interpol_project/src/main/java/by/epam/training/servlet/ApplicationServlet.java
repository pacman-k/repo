package by.epam.training.servlet;


import by.epam.training.ApplicationContext;
import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.security.SecurityService;
import lombok.extern.log4j.Log4j;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.epam.training.ApplicationConstants.CMD_REQ_PARAMETER;

@Log4j
@WebServlet(urlPatterns = "/", loadOnStartup = 1, name = "app")
public class ApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = -898419077104540041L;

    private SecurityService securityService = SecurityService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object command = req.getAttribute(CMD_REQ_PARAMETER);
        String commandName = command != null ? command.toString() : req.getParameter(CMD_REQ_PARAMETER);

        if (commandName == null) {
           req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
           return;
        }
        HttpSession session = req.getSession();
        boolean canExecute = securityService.canExecute(commandName, session.getId());
        if (canExecute) {
            try {
                ServletCommand servletCommand = ApplicationContext.getInstance().getBean(commandName);
                servletCommand.execute(req, resp);
            } catch (CommandException  e) {
                throw new ServletException(e);
            }
        } else {
            resp.sendError(405);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
