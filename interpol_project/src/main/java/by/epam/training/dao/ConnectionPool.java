package by.epam.training.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
    void releaseConnection( Connection connection);
    Connection getConnection();
    int getNumberOfFreeConnections();
    void close();
}
