package by.epam.training.claim;

import by.epam.training.claimstatus.ClaimStatusDto;
import by.epam.training.user.UserDto;
import by.epam.training.wantedPerson.WantedPersonDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDto {
    private Long id;
    private Date dateOfClaim = new Date();
    private double cost;
    private ClaimStatusDto claimStatus = new ClaimStatusDto();
    private UserDto customer = new UserDto();
    private WantedPersonDto wantedPerson = new WantedPersonDto();
    private UserDto executor = new UserDto();

}

