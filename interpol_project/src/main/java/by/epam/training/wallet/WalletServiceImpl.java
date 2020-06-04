package by.epam.training.wallet;


import by.epam.training.core.Bean;
import by.epam.training.dao.DAOException;
import by.epam.training.dao.TransactionSupport;
import by.epam.training.dao.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
@Bean
@TransactionSupport
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {
    WalletDao walletDao;

    @Override
    public void assignWallet(Long walletId, Long userId) {
        try {
            walletDao.assignWallet(walletId, userId);
        } catch (DAOException e) {
            log.error("Cant assign wallet(" + walletId + ") to user(" + userId + ")", e);
        }
    }

    @Override
    public boolean deleteWallet(WalletDto walletDto) {
        try {
            return walletDao.delete(walletDto);
        } catch (DAOException e) {
            log.error("cant delete wallet", e);
            return false;
        }
    }

    @Transactional
    @Override
    public boolean saveWallet(WalletDto walletDto) {
        try {
            walletDao.save(walletDto);
            return true;
        } catch (Exception e) {
            log.error("Cant save wallet : " + walletDto, e);
            return false;
        }
    }

    @Override
    public Optional<WalletDto> getById(Long id) {
        try {
            return Optional.of(walletDao.getById(id));
        } catch (DAOException e) {
            log.error("Cant get wallet by this id : " + id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<WalletDto> getAllWallets() {
        try {
            return walletDao.getAll();
        } catch (DAOException e) {
            log.error("Cant get list of wallet", e);
            return new ArrayList<>();
        }
    }
}
