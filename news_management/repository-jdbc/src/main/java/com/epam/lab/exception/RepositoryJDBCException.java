package com.epam.lab.exception;

public class RepositoryJDBCException extends DAOException {
    public RepositoryJDBCException() {
    }

    public RepositoryJDBCException(String message) {
        super(message);
    }

    public RepositoryJDBCException(String message, Throwable cause) {
        super(message, cause);
    }
}
