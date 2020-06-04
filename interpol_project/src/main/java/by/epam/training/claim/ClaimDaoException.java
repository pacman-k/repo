package by.epam.training.claim;

import by.epam.training.dao.DAOException;

public class ClaimDaoException extends DAOException {
    public ClaimDaoException() {
    }

    public ClaimDaoException(String message) {
        super(message);
    }

    public ClaimDaoException(String message, Exception e) {
        super(message, e);
    }
}
