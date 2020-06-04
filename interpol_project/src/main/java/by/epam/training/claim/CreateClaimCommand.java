package by.epam.training.claim;

import by.epam.training.command.CommandException;
import by.epam.training.command.ServletCommand;
import by.epam.training.core.Bean;
import by.epam.training.user.UserDto;
import by.epam.training.validator.CreateClaimValidator;
import by.epam.training.validator.ValidationResult;
import by.epam.training.wantedPerson.WantedPersonDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static by.epam.training.ApplicationConstants.*;

@Log4j
@Bean(name = CREATE_CLAIM_CMD_NAME)
@AllArgsConstructor
public class CreateClaimCommand implements ServletCommand {
    ClaimService claimService;
    CreateClaimValidator createClaimValidator;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            ValidationResult result = createClaimValidator.getValidationResult(req);
            ClaimDto claimDto = parseClaimToDto(req);
            if (result.isValid()) {
                claimService.registerClaim(claimDto);
                resp.sendRedirect(req.getContextPath());
            } else {
                req.setAttribute("pers", claimDto.getWantedPerson());
                req.setAttribute("error", result.getAllVales());
                req.setAttribute("viewName", CREATE_CLAIM_VIEW_CMD_NAME);
                req.getRequestDispatcher("/jsp/layout.jsp").forward(req, resp);
            }
        } catch (ServletException | IOException e) {
            log.error("Failed create claim :" + e.getMessage(), e);
            throw new CommandException("Claim create command", e);
        }
    }

    private ClaimDto parseClaimToDto(HttpServletRequest req) throws ServletException {
        Map<String, Object> resMap = (Map<String, Object>) req.getAttribute("requestMap");
        ClaimDto claimDto = new ClaimDto();
        final String firstName = getStrFromMap(resMap, FIRST_NAME_ATTRIBUTE);
        final String lastName = getStrFromMap(resMap, LAST_NAME_ATTRIBUTE);
        final String byname = getStrFromMap(resMap, BYNAME_ATTRIBUTE);
        final String country = getStrFromMap(resMap, COUNTRY_ATTRIBUTE);
        final String birthday = getStrFromMap(resMap, BIRTHDAY_ATTRIBUTE);
        final byte[] photo = resMap.containsKey(PHOTO_ATTRIBUTE) ? (byte[]) resMap.get(PHOTO_ATTRIBUTE) : null;
        final String description = getStrFromMap(resMap, DESCRIPTION_ATTRIBUTE);
        WantedPersonDto wantedPersonDto = WantedPersonDto.builder().firstName(firstName)
                .lastName(lastName).byName(byname).country(country)
                .photo(photo).description(description).build();
        try {
            final Long userId = Long.parseLong(getStrFromMap(resMap, "userId"));
            claimDto.setCustomer(UserDto.builder().id(userId).build());
        } catch (NumberFormatException e){
            throw new ServletException("cant get id of customer from request");
        }
        String[] dateArr = birthday != null ? birthday.split("-") : new String[0];
        if (dateArr.length == 3) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(dateArr[0].trim()));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateArr[1].trim()));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArr[2].trim()));
            wantedPersonDto.setBirthday(new Date(calendar.getTimeInMillis()));
        }
        claimDto.setWantedPerson(wantedPersonDto);
        return claimDto;
    }


    private static String getStrFromMap(Map<String, Object> map, String key) {
        return map.containsKey(key) ? map.get(key).toString() : null;
    }
}
