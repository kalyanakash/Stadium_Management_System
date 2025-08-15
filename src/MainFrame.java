
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public void showRegisterPanel() {
        registerPanel = new RegisterPanel(this);
        cardPanel.add(registerPanel, "register");
        cardLayout.show(cardPanel, "register");
    }

    private RegisterPanel registerPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private WelcomePanel welcomePanel;
    private LoginPanel loginPanel;
    private AdminPanel adminPanel;
    private UserPanel userPanel;

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
        registerPanel = null;
        loginPanel = null;
        adminPanel = new AdminPanel(this);
        cardPanel.add(adminPanel, "admin");

        // userPanel will be initialized after login with username

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
        userPanel = new UserPanel(this, username);
        cardPanel.add(userPanel, "user");
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
