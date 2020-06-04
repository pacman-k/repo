package by.epam.training.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {
    private Long walletId;
    private Double walletValue;
}
