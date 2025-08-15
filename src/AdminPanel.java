import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    public AdminPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tabs.setBackground(new Color(236, 240, 241));
        tabs.addTab("Events", new EventPanel());
        tabs.addTab("Tickets", new TicketPanel());
        tabs.addTab("Customers", new CustomerPanel());
        tabs.addTab("Registered Users", new RegisteredUsersPanel());

        // Add Seat Prices tab
        tabs.addTab("Seat Prices", new SeatPricesPanel());
        add(tabs, BorderLayout.CENTER);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setBackground(new Color(52, 152, 219));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> mainFrame.showWelcomePanel());

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> mainFrame.showWelcomePanel());

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.add(btnBack);
        navPanel.add(btnLogout);
        add(navPanel, BorderLayout.SOUTH);
    }
}
