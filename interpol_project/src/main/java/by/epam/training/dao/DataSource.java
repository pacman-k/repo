package by.epam.training.dao;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection() throws DataSourceException;
    void close();
}
