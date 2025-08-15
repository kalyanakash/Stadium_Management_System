import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame mainFrame, String role) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(role.substring(0, 1).toUpperCase() + role.substring(1) + " Login"), gbc);

        gbc.gridy++;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField tfUsername = new JTextField(15);
        add(tfUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField tfPassword = new JPasswordField(15);
        add(tfPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnLogin = new JButton("Login");
        add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String username = tfUsername.getText();
            String password = new String(tfPassword.getPassword());
            try (Connection con = DBUtil.getConnection();
                    PreparedStatement ps = con
                            .prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role=?")) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if ("admin".equals(role)) {
                        mainFrame.showAdminPanel();
                    } else {
                        mainFrame.showUserPanel(username);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials or role.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage());
            }
        });

        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setBackground(new Color(52, 152, 219));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> mainFrame.showWelcomePanel());
        add(btnBack, gbc);
    }
}
