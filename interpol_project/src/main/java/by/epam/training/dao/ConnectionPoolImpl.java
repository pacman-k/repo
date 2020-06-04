package by.epam.training.dao;

import by.epam.training.util.PropertiesWorkerException;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolImpl implements ConnectionPool {
    private final static Logger LOGGER = LogManager.getLogger(ConnectionPoolImpl.class);

    private static ConnectionPool instance = null;
    private final static String DB_PROP_NAME = "database";
    private final static int INITIAL_POOL_SIZE = 10;
    private static final Lock CREATOR_LOCK = new ReentrantLock();

    private Condition condition;
    private final Lock lock;
    private Queue<Connection> connectionPool;
    private Queue<Connection> usedConnections = new LinkedList<>();
    private Driver driver;

    private ConnectionPoolImpl() {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(DB_PROP_NAME);
            String driverClassName = resourceBundle.getString("db.driverClassName");
            Class driverClass = Class.forName(driverClassName);
            driver = (Driver) driverClass.newInstance();
            String url = resourceBundle.getString("db.url");
            String userName = resourceBundle.getString("db.username");
            String password = resourceBundle.getString("db.password");
            connectionPool = new LinkedList<>();
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                connectionPool.add(createConnection(url, userName, password));
            }
        } catch (SQLException | ConnectionPoolException | PropertiesWorkerException |
                ClassNotFoundException |InstantiationException |IllegalAccessException e) {
            LOGGER.log(Level.ERROR, "cant create connection pool", e);
            throw new ConnectionPoolException("Cant create Connection pool : " + e.getMessage(), e);
        }
        lock = new ReentrantLock();
        condition = lock.newCondition();
        LOGGER.log(Level.INFO, "connection pool has been created");
    }

    public static ConnectionPool getInstance() {
        CREATOR_LOCK.lock();
        try {
            if (instance == null) {
                instance = new ConnectionPoolImpl();
            }
        } finally {
            CREATOR_LOCK.unlock();
        }
        return instance;
    }

    private static Connection createConnection(String url, String userName, String password) throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }


    @Override
    public Connection getConnection() {
        lock.lock();
        try {
            try {
                while (connectionPool.isEmpty()) {
                    condition.await();
                }
                Connection connection = connectionPool.remove();
                usedConnections.add(connection);
                return (Connection) Proxy.newProxyInstance(getClass().getClassLoader(),
                        new Class[]{Connection.class}, (proxy, method, args) -> {
                            if (method.getName().equals("close")) {
                                connection.setAutoCommit(true);
                                releaseConnection(connection);
                                return null;
                            }
                            return usedConnections.contains(connection) ? method.invoke(connection, args) : null;
                        });
            } catch (NoSuchElementException | InterruptedException e) {
                throw new ConnectionPoolException("Problems with getting connection : " + e.getMessage(), e);
            }
        } finally {
            lock.unlock();
        }
    }


    public void releaseConnection(Connection connection) {
        lock.lock();
        try {
            if (usedConnections.remove(connection)) {
                connectionPool.add(connection);
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getNumberOfFreeConnections() {
        return connectionPool.size();
    }

    @Override
    public void close(){
        try {
            for (Connection connection : connectionPool) {
                connection.close();
            }
            for (Connection connection : usedConnections) {
                connection.close();
            }
            instance = null;
        } catch (SQLException e){
            throw new ConnectionPoolException("connection pool cant be closed ", e);
        }
    }


}
