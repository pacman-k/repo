package by.epam.training.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimEntity {
    private Long id;
    private Timestamp dateOfClaim;
    private Double cost;
    private Long statusId;
    private Long userAccountId;
    private Long wantedPersonId;
    private Long founderId;

}
