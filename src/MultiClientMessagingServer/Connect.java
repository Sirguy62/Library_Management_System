package MultiClientMessagingServer;

import java.sql.Connection;
import java.sql.SQLException;

public class Connect {

    public static void main(String[] args) {

        try (Connection connection = DatabaseConnect.getConnection()) {
            System.out.println("Connected to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
