package by.epam.training.dao;

import by.epam.training.core.Bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;

@Data
@AllArgsConstructor
@Bean
public class ConnectionManagerImpl implements ConnectionManager {
    private final static Logger LOGGER = LogManager.getLogger(ConnectionManagerImpl.class);


    private DataSource dataSource;
    private TransactionManager transactionManager;


    @Override
    public Connection getConnection() throws ConnectionManagerException {
        try {
            Connection connection = transactionManager.getConnection();
            return connection != null ? connection : dataSource.getConnection();
        } catch (DataSourceException e){
            LOGGER.log(Level.ERROR, "cant get connection : " + e.getMessage(), e);
            throw new ConnectionManagerException("cant get connection", e);
        }
    }


}
