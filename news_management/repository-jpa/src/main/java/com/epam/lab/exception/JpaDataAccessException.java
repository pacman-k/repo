package com.epam.lab.exception;

import org.springframework.dao.DataAccessException;

public class JpaDataAccessException extends DataAccessException {

    public JpaDataAccessException(String msg) {
        super(msg);
    }

    public JpaDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
