package organise.creditrisk.infrastructure.db;

public class DatabaseConfig {

    public static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:postgresql://localhost:5432/creditrisk"
            );

    public static final String USER =
            System.getenv().getOrDefault(
                    "DB_USER",
                    "postgres"
            );

    public static final String PASSWORD =
            System.getenv().getOrDefault(
                    "DB_PASSWORD",
                    "postgres"
            );

    public static final int MAX_POOL_SIZE = 10;
}