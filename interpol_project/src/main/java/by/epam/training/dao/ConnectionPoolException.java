package by.epam.training.dao;

public class ConnectionPoolException extends RuntimeException {

    public ConnectionPoolException() {

    }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(String message, Exception e) {
        super(message, e);
    }
}

