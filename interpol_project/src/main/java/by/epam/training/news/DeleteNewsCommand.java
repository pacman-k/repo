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

import static by.epam.training.ApplicationConstants.DELETE_NEWS_CMD;

@Log4j
@Bean(name = DELETE_NEWS_CMD)
@AllArgsConstructor
@Getter
public class DeleteNewsCommand implements ServletCommand {
    NewsService newsService;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        Long newsId = Long.parseLong(req.getParameter("newsId"));
        try {
           Optional<NewsDto> newsDto = newsService.findById(newsId);
            newsDto.ifPresent(dto -> newsService.deleteNews(dto));
            resp.sendRedirect(req.getContextPath() + "/?commandName=" + ApplicationConstants.VIEW_NEWS_CMD_NAME);
        }catch ( IOException e){
            log.error("Something went wrong in DeleteNewsCommand");
            throw new CommandException("ERROR on DeleteNewsCommand");
        }
    }
}
