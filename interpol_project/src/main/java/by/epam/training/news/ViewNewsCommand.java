package by.epam.training.news;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static by.epam.training.ApplicationConstants.VIEW_NEWS_CMD_NAME;

@Log4j
@Bean(name = VIEW_NEWS_CMD_NAME)
public class ViewNewsCommand implements ServletCommand {
    private NewsService newsService;

    public ViewNewsCommand (NewsService newsService){
        this.newsService = newsService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        final List<NewsDto> news = newsService.getAllNews();
        req.setAttribute("news", news);
        try {
            req.setAttribute("viewName", VIEW_NEWS_CMD_NAME);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong...", e);
            throw new CommandException("cant get news list", e);
        }
    }
}
