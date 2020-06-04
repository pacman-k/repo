package by.epam.training.user;

import by.epam.training.ApplicationConstants;
import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.news.NewsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static by.epam.training.ApplicationConstants.EDIT_USER_VIEW_CMD;

@Log4j
@Bean(name = EDIT_USER_VIEW_CMD)
@AllArgsConstructor
@Getter
public class EditUserViewCommand implements ServletCommand {
    UserService userService;
        @Override
        public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
            Long userId = Long.parseLong(req.getParameter("userId"));
            try {
                Optional<UserDto> userDto = userService.findById(userId);
                if (userDto.isPresent()) {
                    req.setAttribute("oldUser", userDto.get());
                    req.setAttribute("viewName", EDIT_USER_VIEW_CMD);
                }
                req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                log.error("Something went wrong...", e);
                throw new CommandException("ERROR in EditNewsViewCommand", e);
            }

        }
}
