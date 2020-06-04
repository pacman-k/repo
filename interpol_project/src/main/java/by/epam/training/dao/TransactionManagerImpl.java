package by.epam.training.dao;

import by.epam.training.core.Bean;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

@Bean
public class TransactionManagerImpl implements TransactionManager {
    private final static Logger LOGGER = LogManager.getLogger(TransactionManagerImpl.class);

    private DataSource dataSource;
    private ThreadLocal<Connection> localConnection = new ThreadLocal<>();

    public TransactionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void beginTransaction() throws TransactionManagerException {
        if (localConnection.get() == null) {
            try {
                Connection connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                localConnection.set(connection);
            } catch (DataSourceException | SQLException e) {
                LOGGER.log(Level.ERROR, "Cant begin transaction : " + e.getMessage(), e);
                throw new TransactionManagerException("Cant begin transaction", e);
            }
        } else {
            LOGGER.log(Level.INFO, "transaction had been started");
        }
    }

    @Override
    public void commitTransaction() throws TransactionManagerException {
        try {
            Connection connection = localConnection.get();
            if (connection != null) {
                connection.commit();
                connection.close();
            }
            localConnection.remove();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Cant commit transaction : " + e.getMessage(), e);
            throw new TransactionManagerException("cant commit transaction", e);
        }
    }

    @Override
    public void rollbackTransaction() throws TransactionManagerException {
        try {
            Connection connection = localConnection.get();
            if (connection != null) {
                connection.rollback();
                connection.close();
            }
            localConnection.remove();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Cant rollback transaction : " + e.getMessage(), e);
            throw new TransactionManagerException("cant rollback transaction", e);

        }

    }

    @Override
    public Connection getConnection() {
        Connection connection = localConnection.get();
        if (connection != null) {
            return (Connection) Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class[]{Connection.class}, (proxy, method, args) -> {
                        String methodName = method.getName();
                        if (methodName.equals("close") || methodName.equals("commit") || methodName.equals("rollback")) {
                            return null;
                        } else {
                            return method.invoke(connection, args);
                        }

                    });
        } else {
            return null;
        }
    }
}




