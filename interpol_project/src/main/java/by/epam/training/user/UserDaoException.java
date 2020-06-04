package by.epam.training.user;

import by.epam.training.dao.DAOException;

public class UserDaoException extends DAOException {
    public UserDaoException(String message) {
        super(message);
    }

    public UserDaoException(String message, Exception e) {
        super(message, e);
    }

}
