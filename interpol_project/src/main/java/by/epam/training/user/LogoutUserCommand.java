package by.epam.training.user;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.security.SecurityService;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static by.epam.training.ApplicationConstants.LOGOUT_USER_CMD_NAME;

@AllArgsConstructor
@Bean(name = LOGOUT_USER_CMD_NAME)
public class LogoutUserCommand implements ServletCommand {
    private SecurityService securityService;
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        HttpSession session = req.getSession(true);
        if (session != null) {
            securityService.logout(session.getId());
            session.invalidate();
        }
        try {
            resp.sendRedirect(req.getContextPath());
        } catch (IOException e) {
            throw new CommandException("cant get home ",e);
        }
    }
}
