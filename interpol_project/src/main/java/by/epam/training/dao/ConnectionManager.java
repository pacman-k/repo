package by.epam.training.dao;

import java.sql.Connection;

public interface ConnectionManager {
    Connection getConnection() throws ConnectionManagerException;
}
