package by.epam.training.claim;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.epam.training.ApplicationConstants.CONFIRM_CLAIM_CMD_NAME;

@Log4j
@Bean(name = CONFIRM_CLAIM_CMD_NAME)
@AllArgsConstructor
@Getter
public class ConfirmClaimCommand implements ServletCommand {
    ClaimService claimService;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            Long wantedPersId = Long.parseLong(req.getParameter("personId"));
            ClaimDto claim = claimService.findByWantedPers(wantedPersId).orElseThrow(() ->
                    new CommandException("cant find claim by such wanted pers id :" + wantedPersId));
            claimService.setActualStatus(claim);
            String cmd = req.getParameter("viewName");
            resp.sendRedirect(req.getContextPath() + "/?commandName=" + cmd);
        } catch (IOException e) {
            log.error("some problems in ConfirmClaimCommand");
            throw new CommandException("cant confirm claim", e);
        }
    }
}
