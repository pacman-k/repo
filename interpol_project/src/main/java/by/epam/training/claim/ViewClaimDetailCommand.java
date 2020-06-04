package by.epam.training.claim;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.wantedPerson.WantedPersonDto;
import by.epam.training.wantedPerson.WantedPersonService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.epam.training.ApplicationConstants.VIEW_CLAIM_DETAILS_CMD_NAME;

@Log4j
@Bean(name = VIEW_CLAIM_DETAILS_CMD_NAME)
@AllArgsConstructor
@Getter
public class ViewClaimDetailCommand implements ServletCommand {
    ClaimService claimService;
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        Long id = Long.parseLong(req.getParameter("persId"));
        ClaimDto claimDto = claimService.findByWantedPers(id).orElseThrow(()->  new CommandException("cant find claim by such wantedPers id :" + id));
        try {
            req.setAttribute("claim", claimDto);
            req.setAttribute("pers", claimDto.getWantedPerson());
            req.setAttribute("customerId", claimDto.getCustomer().getId());
            req.setAttribute("viewName", VIEW_CLAIM_DETAILS_CMD_NAME);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.error("Something went wrong...", e);
            throw new CommandException("ERROR in RegisterViewUserCommand",e);
        }
    }
}
