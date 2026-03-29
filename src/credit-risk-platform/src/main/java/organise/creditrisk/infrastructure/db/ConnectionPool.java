package organise.creditrisk.infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static ConnectionPool instance;
    private final BlockingQueue<Connection> pool;

    private ConnectionPool() {
        pool = new ArrayBlockingQueue<>(DatabaseConfig.MAX_POOL_SIZE);

        try {
            for (int i = 0; i < DatabaseConfig.MAX_POOL_SIZE; i++) {
                Connection conn = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );
                pool.offer(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize DB pool", e);
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for DB connection", e);
        }
    }

    public void releaseConnection(Connection connection) {
        pool.offer(connection);
    }
}