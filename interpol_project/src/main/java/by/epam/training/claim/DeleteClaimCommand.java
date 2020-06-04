package by.epam.training.claim;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.validator.DeleteClaimValidator;
import by.epam.training.validator.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static by.epam.training.ApplicationConstants.DELETE_CLAIM_CMD_NAME;

@Log4j
@Bean(name = DELETE_CLAIM_CMD_NAME)
@AllArgsConstructor
@Getter
public class DeleteClaimCommand implements ServletCommand {
    DeleteClaimValidator deleteClaimValidator;
    ClaimService claimService;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        Long id = Long.parseLong(req.getParameter("personId"));
        req.setAttribute("id", id);
        String cmd = req.getParameter("viewName");
       ValidationResult result = deleteClaimValidator.getValidationResult(req);
       if (result.isValid()) {
           Optional<ClaimDto> claimDto = claimService.findByWantedPers(id);
           claimDto.ifPresent(dto -> claimService.deleteClaim(dto));
       }
        try {
            resp.sendRedirect(req.getContextPath() + "/?commandName=" + cmd);
        } catch (IOException e) {
            log.error("some problems in DeleteClaimCommand");
            throw new CommandException("cant delete claim", e);
        }

    }
}
