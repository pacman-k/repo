package by.epam.training.news;

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

import static by.epam.training.ApplicationConstants.ADD_NEWS_VIEW_CMD;

@Log4j
@Bean(name = ADD_NEWS_VIEW_CMD)
@AllArgsConstructor
@Getter
public class AddNewsViewCommand implements ServletCommand {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            req.setAttribute("viewName", ADD_NEWS_VIEW_CMD);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong in AddNewsViewCommand");
            throw new CommandException("ERROR in AddNewsViewCommand", e);
        }

    }
}
