package by.epam.training.dao;

public class ConnectionManagerException extends DAOException {
    public ConnectionManagerException() {

    }

    public ConnectionManagerException(String message) {
        super(message);
    }

    public ConnectionManagerException(String message, Exception e) {
        super(message, e);
    }
}
