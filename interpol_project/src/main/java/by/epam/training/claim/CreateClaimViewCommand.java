package by.epam.training.claim;

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

import static by.epam.training.ApplicationConstants.CREATE_CLAIM_VIEW_CMD_NAME;

@Log4j
@Bean(name = CREATE_CLAIM_VIEW_CMD_NAME)
@AllArgsConstructor
@Getter
public class CreateClaimViewCommand implements ServletCommand {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            req.setAttribute("viewName", CREATE_CLAIM_VIEW_CMD_NAME);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong...", e);
            throw new CommandException("ERROR in CreateClaimViewCommand", e);
        }
    }
}
