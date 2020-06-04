package by.epam.training.claimstatus;

import by.epam.training.dao.DAOException;

public class StatusDaoException extends DAOException {
    public StatusDaoException(String message) {
        super(message);
    }

    public StatusDaoException(String message, Exception e) {
        super(message, e);
    }
}
