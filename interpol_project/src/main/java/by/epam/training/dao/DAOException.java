package by.epam.training.dao;

public class DAOException extends Exception {

    public DAOException() {

    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Exception e) {
        super(message, e);
    }
}
