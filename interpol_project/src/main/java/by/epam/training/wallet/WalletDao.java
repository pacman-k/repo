package by.epam.training.wallet;

import by.epam.training.dao.CrudDao;
import by.epam.training.dao.DAOException;

public interface WalletDao extends CrudDao<WalletDto, Long> {
    void assignWallet(Long walletId, Long userId) throws DAOException;

}
