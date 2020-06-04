package by.epam.training.dao;

import by.epam.training.core.Bean;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;

@Bean
@Getter
@Setter
public class DataSourceImpl implements DataSource {
    private final static Logger LOGGER = LogManager.getLogger(DataSourceImpl.class);

    private ConnectionPool connectionPool;

    public DataSourceImpl(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() throws DataSourceException {
        try {
            return connectionPool.getConnection();
        } catch (ConnectionPoolException e){
            LOGGER.log(Level.ERROR,"Cant get connection from connection pool : " + e.getMessage(), e );
            throw new DataSourceException("Cant get connection from connection pool", e);
        }
    }

    @Override
    public void close() {
            connectionPool.close();
    }

}
