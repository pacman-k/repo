package com.epam.lab.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class PersistenceExceptionTranslatorImpl implements PersistenceExceptionTranslator {
    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        return new JpaDataAccessException(e.getMessage(), e.getCause());
    }
}
