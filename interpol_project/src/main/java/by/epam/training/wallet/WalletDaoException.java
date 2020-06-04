package by.epam.training.wallet;

import by.epam.training.dao.DAOException;

public class WalletDaoException extends DAOException {
    public WalletDaoException(String message) {
        super(message);
    }

    public WalletDaoException(String message, Exception e) {
        super(message, e);
    }
}
