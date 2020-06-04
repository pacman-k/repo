package by.epam.training.claim;

import by.epam.training.claimstatus.ClaimStatusDto;
import by.epam.training.claimstatus.StatusService;
import by.epam.training.claimstatus.StatusTypes;
import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.wantedPerson.WantedPersonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static by.epam.training.ApplicationConstants.VIEW_CLAIMS_CMD_NAME;

@Log4j
@Bean(name = VIEW_CLAIMS_CMD_NAME)
@AllArgsConstructor
@Getter
public class ViewActualClaimsCommand implements ServletCommand {
    ClaimService claimService;
    StatusService statusService;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        ClaimStatusDto status = statusService.getStatusByName(StatusTypes.ACTUAL.toString()).orElse(null);
        final List<ClaimDto> claims = claimService.findByStatus(status);
        final List<WantedPersonDto> wantedPersons = claims.stream().map(ClaimDto::getWantedPerson).collect(Collectors.toList());
        req.setAttribute("persons", wantedPersons);
        req.setAttribute("type", "actual");
        try {
            req.setAttribute("viewName", VIEW_CLAIMS_CMD_NAME);
            req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
        }catch (ServletException | IOException e){
            log.error("Something went wrong...", e);
            throw new CommandException("cant get claims list",e);
        }

    }
}
