package by.epam.training.dao;

public class TransactionManagerException extends RuntimeException {

    public TransactionManagerException() {

    }

    public TransactionManagerException(String message) {
        super(message);
    }

    public TransactionManagerException(String message, Exception e) {
        super(message, e);
    }
}
