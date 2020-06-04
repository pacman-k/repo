package by.epam.training.news;

import by.epam.training.dao.DAOException;

public class NewsDaoException extends DAOException {
    public NewsDaoException(String message) {
        super(message);
    }

    public NewsDaoException(String message, Exception e) {
        super(message, e);
    }
}
