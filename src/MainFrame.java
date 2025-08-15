
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private WelcomePanel welcomePanel;
    private LoginPanel loginPanel;
    private JPanel adminPanel;
    private JPanel userPanel;

    public MainFrame() {
        setTitle("Stadium Management System");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(44, 62, 80));
        JLabel title = new JLabel("Stadium Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(title);
        add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        welcomePanel = new WelcomePanel(this);
        cardPanel.add(welcomePanel, "welcome");

        // Placeholders for login/admin/user panels
        loginPanel = null;
        adminPanel = new JPanel(new BorderLayout());
        adminPanel.add(new JLabel("Admin Dashboard (TODO)"), BorderLayout.CENTER);
        cardPanel.add(adminPanel, "admin");

        userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JLabel("User Dashboard (TODO)"), BorderLayout.CENTER);
        cardPanel.add(userPanel, "user");

        add(cardPanel, BorderLayout.CENTER);
        showWelcomePanel();
    }

    public void showWelcomePanel() {
        cardLayout.show(cardPanel, "welcome");
    }

    public void showLoginPanel(String role) {
        loginPanel = new LoginPanel(this, role);
        cardPanel.add(loginPanel, "login");
        cardLayout.show(cardPanel, "login");
    }

    public void showAdminPanel() {
        cardLayout.show(cardPanel, "admin");
    }

    public void showUserPanel(String username) {
        cardLayout.show(cardPanel, "user");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
            new MainFrame().setVisible(true);
        });
    }
}
