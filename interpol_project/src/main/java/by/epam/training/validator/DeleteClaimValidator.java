package by.epam.training.validator;

import by.epam.training.claim.ClaimDto;
import by.epam.training.claim.ClaimService;
import by.epam.training.core.Bean;

import javax.servlet.http.HttpServletRequest;

@Bean(name = "DeleteClaimValidator")
public class DeleteClaimValidator implements ReqValidator {
    ClaimService claimService;

    @Override
    public ValidationResult getValidationResult(HttpServletRequest request) {

        ValidationResult validationResult = new ValidationResult();

        String role = request.getSession().getAttribute("role").toString();
        if ("admin".equals(role)) {
            return validationResult;
        }
        Long id;
        try {
            id = Long.parseLong(request.getAttribute("id").toString());
        } catch (Exception e) {
            validationResult.addError("id", "incorrect id of wanted person");
            return validationResult;
        }
        ClaimDto claimDto = claimService.findByWantedPers(id).get();
        if (claimDto == null) {
            validationResult.addError("claim", "there is no such claim");
            return validationResult;
        }
        Long sesId = Long.parseLong(request.getSession().getAttribute("userId").toString());
        if (!sesId.equals(id)) {
            validationResult.addError("rights", "you cant delete this claim");
        }
        return validationResult;

    }

    private static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }
}
