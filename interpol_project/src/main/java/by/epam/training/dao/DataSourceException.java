package by.epam.training.dao;

public class DataSourceException extends DAOException {

    public DataSourceException() {

    }

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Exception e) {
        super(message, e);
    }
}
