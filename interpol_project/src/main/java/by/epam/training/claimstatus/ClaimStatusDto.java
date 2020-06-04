package by.epam.training.claimstatus;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClaimStatusDto {
    private Long id;
    private StatusTypes status = StatusTypes.UNDER_CONSIDERATION;

    @Override
    public String toString(){
        return status.toString();
    }
}
