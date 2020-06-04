package by.epam.training.dao;

import java.sql.Connection;

public interface TransactionManager {
    void beginTransaction() throws  TransactionManagerException;
    void commitTransaction() throws TransactionManagerException;
    void rollbackTransaction() throws TransactionManagerException;
    Connection getConnection();
}
