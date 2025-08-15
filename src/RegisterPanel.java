import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel(MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("User Registration"), gbc);

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
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField tfEmail = new JTextField(15);
        add(tfEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        JTextField tfPhone = new JTextField(15);
        add(tfPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnRegister = new JButton("Register");
        add(btnRegister, gbc);

        btnRegister.addActionListener(e -> {
            String username = tfUsername.getText();
            String password = new String(tfPassword.getPassword());
            String email = tfEmail.getText();
            String phone = tfPhone.getText();
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields required.");
                return;
            }
            try (Connection con = DBUtil.getConnection()) {
                // Insert into users table
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users (username, password, role, email, phone) VALUES (?, ?, 'user', ?, ?)")) {
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.setString(3, email);
                    ps.setString(4, phone);
                    ps.executeUpdate();
                }
                // Insert into customer table only if not already present
                try (PreparedStatement ps2 = con.prepareStatement(
                        "INSERT IGNORE INTO customer (name, email, phone) VALUES (?, ?, ?)")) {
                    ps2.setString(1, username);
                    ps2.setString(2, email);
                    ps2.setString(3, phone);
                    ps2.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
                mainFrame.showLoginPanel("user");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Registration error: " + ex.getMessage());
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setBackground(new Color(52, 152, 219));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> mainFrame.showWelcomePanel());
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnBack, gbc);
    }
}
