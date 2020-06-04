package by.epam.training.dao;

import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@Log4j
@RunWith(JUnit4.class)
public class ConnectionPoolImplTest {
    ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

    @Test
    public void testGetInstanceNotNull() {
        assertNotNull(connectionPool);
    }

    @Test
    public void testGetInstanceSame() {
        ConnectionPool actualInstance = ConnectionPoolImpl.getInstance();
        assertSame(actualInstance, connectionPool);
    }

    @Test
    public void testCloseConnectionNormal() throws Exception {
        Set<Connection> connections = new HashSet<>(10);
        for (int i = 0; i < 10; i++) {
            connections.add(connectionPool.getConnection());
        }
        assertEquals(0, connectionPool.getNumberOfFreeConnections());
        for (Connection connection : connections) {
            connection.close();
        }
        assertEquals(connectionPool.getNumberOfFreeConnections(), 10);

    }

}
