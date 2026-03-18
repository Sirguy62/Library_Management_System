package MultiClientMessagingServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageDAO {

    public static void saveMessage(String sender, String content) {

        String sql =
                "INSERT INTO messages(sender, content) VALUES (?, ?)";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sender);
            stmt.setString(2, content);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getRecentMessages(int limit) {

        List<String> messages = new ArrayList<>();

        String sql =
                "SELECT sender, content FROM messages ORDER BY id DESC LIMIT ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("sender");
                String content = rs.getString("content");

                messages.add(sender + ": " + content);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.reverse(messages); // show oldest first
        return messages;
    }
}
