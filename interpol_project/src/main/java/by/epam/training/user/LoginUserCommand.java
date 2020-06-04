package by.epam.training.user;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.security.CryptographyManager;
import by.epam.training.security.SecurityService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static by.epam.training.ApplicationConstants.LOGIN_ATTRIBUTE;
import static by.epam.training.ApplicationConstants.ROLE_ATTRIBUTE;

import static by.epam.training.ApplicationConstants.*;

@Log4j
@Bean(name = LOGIN_CMD_NAME)
@AllArgsConstructor
@Getter
public class LoginUserCommand implements ServletCommand {
    private UserService userService;
    private SecurityService securityService;
    private CryptographyManager cryptographyManager;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
      final   String login = req.getParameter(LOGIN_ATTRIBUTE);
      final   String password = req.getParameter(PASSWORD_ATTRIBUTE);
       try {
           Optional<UserDto> user = userService.findByLogin(login);
           boolean isThisUser = user.isPresent() && password.equals(cryptographyManager.decode(user.get().getPassword()));
           if(isThisUser){
               UserDto userDto = user.get();
               if (userDto.getRoleDto().getRoleTypes().toString().equals("locked")){
                   req.setAttribute("error", "Sorry, you were locked");
                   req.setAttribute("viewName", LOGIN_VIEW_CMD_NAME);
                   req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
                   return;
               }
               HttpSession session = req.getSession();
               session.setAttribute(LOGIN_ATTRIBUTE, userDto.getLogin());
               session.setAttribute(ROLE_ATTRIBUTE, userDto.getRoleDto().getRoleTypes().toString());
               session.setAttribute("userId", userDto.getId());
               securityService.login(session.getId(), userDto);
               resp.sendRedirect(req.getContextPath());
           }else {
               req.setAttribute("login", login);
               req.setAttribute("error", "Account not found! Check out input login and password!");
               req.setAttribute("viewName", LOGIN_VIEW_CMD_NAME);
               req.getRequestDispatcher("jsp/layout.jsp").forward(req, resp);
           }
       }catch (IOException | ServletException e){
           log.error("Failed to login user");
           throw new CommandException("FAIL",e);
       }

    }
}
