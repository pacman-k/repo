package by.epam.training.role;

import by.epam.training.dao.DAOException;

public class RoleDaoException extends DAOException {
    public RoleDaoException(String message) {
        super(message);
    }

    public RoleDaoException(String message, Exception e) {
        super(message, e);
    }
}
