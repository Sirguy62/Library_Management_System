package organise.creditrisk.infrastructure.db;

import java.sql.Connection;

public abstract class BaseRepository {

    protected Connection getConnection() {
        return ConnectionPool.getInstance().getConnection();
    }

    protected void release(Connection connection) {
        ConnectionPool.getInstance().releaseConnection(connection);
    }
}