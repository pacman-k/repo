package by.epam.training.news;


import by.epam.training.ApplicationConstants;
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
import java.util.Optional;

import static by.epam.training.ApplicationConstants.EDIT_NEWS_VIEW_CMD;

@Log4j
@Bean(name = EDIT_NEWS_VIEW_CMD)
@AllArgsConstructor
@Getter
public class EditNewsViewCommand implements ServletCommand {
    NewsService newsService;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        Long newsId = Long.parseLong(req.getParameter("newsId"));
        try {
            Optional<NewsDto> newsDto = newsService.findById(newsId);
            if (newsDto.isPresent()) {
                req.setAttribute("oldNews", newsDto.get());
                req.setAttribute("viewName", EDIT_NEWS_VIEW_CMD);
            } else {
                req.setAttribute("viewName", ApplicationConstants.VIEW_NEWS_CMD_NAME);
            }
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong...", e);
            throw new CommandException("ERROR in EditNewsViewCommand", e);
        }

    }
}
