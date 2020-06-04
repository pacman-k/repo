package by.epam.training.user;

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

import static by.epam.training.ApplicationConstants.LOCK_USER_CMD;


@Log4j
@Bean(name = LOCK_USER_CMD)
@AllArgsConstructor
@Getter
public class LockUserCommand implements ServletCommand {
    UserService userService;
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            Long id = Long.parseLong(req.getParameter("userId"));
            String cmd = req.getParameter("viewName");
            Optional<UserDto> userDto = userService.findById(id);
            userDto.ifPresent(dto -> userService.lockUser(dto));
            resp.sendRedirect(req.getContextPath() + "/?commandName=" + cmd);
        } catch ( IOException e){
            log.error("some problems in LockUserCommand");
            throw new CommandException("cant lock user",e);
        }
    }
}
