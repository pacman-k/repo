package by.epam.training.wallet;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private Long walletId;
    private Double walletValue = 0.0;
}
