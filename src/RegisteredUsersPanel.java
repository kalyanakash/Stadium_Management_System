import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RegisteredUsersPanel extends JPanel {
    public RegisteredUsersPanel() {
        setLayout(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[] { "User ID", "Username", "Password", "Role", "Email", "Phone" },
                0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load registered users from DB
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT user_id, username, password, role, email, phone FROM users")) {
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("phone")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }
}
