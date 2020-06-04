package by.epam.training.news;

import by.epam.training.ApplicationConstants;
import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.validator.SaveNewsValidator;
import by.epam.training.validator.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import static by.epam.training.ApplicationConstants.EDIT_NEWS_CMD;

@Log4j
@Bean(name = EDIT_NEWS_CMD)
@AllArgsConstructor
@Getter
public class EditNewsCommand implements ServletCommand {
    NewsService newsService;
    SaveNewsValidator saveNewsValidator;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            ValidationResult result = saveNewsValidator.getValidationResult(req);
            NewsDto dto = parseNewsToDto(req);
            if (result.isValid()) {
                newsService.updateNews(dto);
                resp.sendRedirect(req.getContextPath() + "/?commandName=" + ApplicationConstants.VIEW_NEWS_CMD_NAME);
            } else {
                req.setAttribute("oldNews", dto);
                req.setAttribute("error", result.getAllVales());
                req.setAttribute("viewName", ApplicationConstants.EDIT_NEWS_VIEW_CMD);
                req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
            }
        } catch (IllegalStateException | ServletException | IOException e) {
            log.error("Failed edit news", e);
            throw new CommandException("ERROR during AddNewsCommand", e);
        }
    }

    private NewsDto parseNewsToDto(HttpServletRequest request) {
        final Long id = Long.parseLong(request.getParameter("newsId"));
        final String topic = request.getParameter("news.topic");
        final String header = request.getParameter("news.header");
        final String text = request.getParameter("news.text");
        return NewsDto.builder().newsId(id).newsTopic(topic)
                .newsHeading(header).dateOfPost(new Date()).newsText(text).build();
    }
}
