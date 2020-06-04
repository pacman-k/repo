package by.epam.training.wallet;

import by.epam.training.dao.Transactional;

import java.util.List;
import java.util.Optional;

public interface WalletService {
    void assignWallet(Long walletId, Long userId);

    boolean deleteWallet(WalletDto walletDto);

    @Transactional
    boolean saveWallet(WalletDto walletDto);

    Optional<WalletDto> getById(Long id);

    List<WalletDto> getAllWallets ();

}
