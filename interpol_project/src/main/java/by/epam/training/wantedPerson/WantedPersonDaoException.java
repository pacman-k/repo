package by.epam.training.wantedPerson;

import by.epam.training.dao.DAOException;

public class WantedPersonDaoException extends DAOException {
    public WantedPersonDaoException(String message) {
        super(message);
    }

    public WantedPersonDaoException(String message, Exception e) {
        super(message, e);
    }
}
