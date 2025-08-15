import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomePanel extends JPanel {
    public WelcomePanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Welcome to Stadium Portal", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdmin = new JButton("Admin Login");
        JButton btnUser = new JButton("User Login");
        btnPanel.add(btnAdmin);
        btnPanel.add(btnUser);
        add(btnPanel, BorderLayout.CENTER);

        btnAdmin.addActionListener(e -> mainFrame.showLoginPanel("admin"));
        btnUser.addActionListener(e -> mainFrame.showLoginPanel("user"));
    }
}
