import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserDetailsPanel extends JPanel {
    public UserDetailsPanel(String username) {
        setLayout(new BorderLayout());
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        // Load user details from DB
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Username: ").append(rs.getString("username")).append("\n");
                sb.append("Role: ").append(rs.getString("role")).append("\n");
                detailsArea.setText(sb.toString());
            }
        } catch (Exception ex) {
            detailsArea.setText("Error loading user details: " + ex.getMessage());
        }
    }
}
