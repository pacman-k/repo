package by.epam.training.user;

import by.epam.training.role.RoleDto;
import by.epam.training.wallet.WalletDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private RoleDto roleDto = new RoleDto();
   private WalletDto walletDto = new WalletDto();
}
