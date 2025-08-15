
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Stadium Management System");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header panel
        JPanel header = new JPanel();
        header.setBackground(new Color(44, 62, 80));
        JLabel title = new JLabel("Stadium Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Tabbed pane with custom UI
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tabs.setBackground(new Color(236, 240, 241));
        tabs.addTab("Events", new EventPanel());
        tabs.addTab("Customers", new CustomerPanel());
        tabs.addTab("Tickets", new TicketPanel());
        add(tabs, BorderLayout.CENTER);
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
