import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class UserPanel extends JPanel {
    public UserPanel(MainFrame mainFrame, String username) {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tabs.setBackground(new Color(236, 240, 241));
        tabs.addTab("Book Ticket", new TicketPanel());
        tabs.addTab("My Details", new UserDetailsPanel(username));

        // Stadium Tab
        JPanel stadiumPanel = new JPanel(new BorderLayout(10, 10));
        DefaultListModel<String> stadiumListModel = new DefaultListModel<>();
        JList<String> stadiumList = new JList<>(stadiumListModel);
        stadiumList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollPane stadiumScroll = new JScrollPane(stadiumList);
        stadiumPanel.add(stadiumScroll, BorderLayout.WEST);

        DefaultTableModel stadiumDetailsModel = new DefaultTableModel(
                new String[] { "Seat No", "Price", "Available" }, 0);
        JTable stadiumDetailsTable = new JTable(stadiumDetailsModel);
        JScrollPane stadiumDetailsScroll = new JScrollPane(stadiumDetailsTable);
        stadiumPanel.add(stadiumDetailsScroll, BorderLayout.CENTER);

        // Load stadiums from DB
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT stadium_id, stadium_name FROM stadium")) {
            while (rs.next()) {
                String stadiumInfo = rs.getInt("stadium_id") + ": " + rs.getString("stadium_name");
                stadiumListModel.addElement(stadiumInfo);
            }
        } catch (Exception ex) {
            stadiumListModel.addElement("Error loading stadiums: " + ex.getMessage());
        }

        // Load seats and prices for selected stadium
        stadiumList.addListSelectionListener(e -> {
            stadiumDetailsModel.setRowCount(0);
            int idx = stadiumList.getSelectedIndex();
            if (idx < 0)
                return;
            String selected = stadiumListModel.get(idx);
            String stadiumId = selected.split(":")[0];
            try (Connection con = DBUtil.getConnection()) {
                PreparedStatement psSeats = con
                        .prepareStatement("SELECT seat_no, price FROM seats WHERE stadium_id = ?");
                psSeats.setInt(1, Integer.parseInt(stadiumId));
                ResultSet rsSeats = psSeats.executeQuery();
                java.util.Set<String> allSeats = new java.util.HashSet<>();
                java.util.Map<String, String> seatPrices = new java.util.HashMap<>();
                while (rsSeats.next()) {
                    String seatNo = rsSeats.getString("seat_no");
                    String price = rsSeats.getString("price");
                    allSeats.add(seatNo);
                    seatPrices.put(seatNo, price);
                }
                // Find booked seats
                PreparedStatement psBooked = con.prepareStatement("SELECT seat_no FROM ticket");
                ResultSet rsBooked = psBooked.executeQuery();
                java.util.Set<String> bookedSeats = new java.util.HashSet<>();
                while (rsBooked.next()) {
                    bookedSeats.add(rsBooked.getString("seat_no"));
                }
                // Display seats with price and status
                for (String seat : allSeats) {
                    String price = seatPrices.get(seat);
                    if (bookedSeats.contains(seat)) {
                        stadiumDetailsModel.addRow(new Object[] { seat, price, "Booked" });
                    } else {
                        stadiumDetailsModel.addRow(new Object[] { seat, price, "Available" });
                    }
                }
            } catch (Exception ex) {
                stadiumDetailsModel.addRow(new Object[] { "Error", "", ex.getMessage() });
            }
        });

        tabs.addTab("Stadiums", stadiumPanel);

        // New Event Overview Tab
        JPanel eventOverviewPanel = new JPanel(new BorderLayout(10, 10));
        // Event List
        DefaultListModel<String> eventListModel = new DefaultListModel<>();
        JList<String> eventList = new JList<>(eventListModel);
        eventList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollPane eventScroll = new JScrollPane(eventList);
        eventOverviewPanel.add(eventScroll, BorderLayout.WEST);

        // Details Table
        DefaultTableModel detailsModel = new DefaultTableModel(
                new String[] { "Customer", "Seat No", "Price", "Available" }, 0);
        JTable detailsTable = new JTable(detailsModel);
        // Show seat price when user clicks a seat row
        detailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = detailsTable.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    String seatNo = detailsTable.getValueAt(row, 1).toString();
                    String price = detailsTable.getValueAt(row, 2).toString();
                    if ((price == null || price.isEmpty()) && seatNo != null && !seatNo.isEmpty()) {
                        // Try to fetch price from DB
                        try (Connection con = DBUtil.getConnection();
                                PreparedStatement ps = con
                                        .prepareStatement("SELECT price FROM seats WHERE seat_no = ? LIMIT 1")) {
                            ps.setString(1, seatNo);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next()) {
                                price = rs.getString("price");
                                detailsTable.setValueAt(price, row, 2);
                            }
                        } catch (Exception ex) {
                            price = "Error";
                        }
                    }
                    if (seatNo != null && !seatNo.isEmpty() && price != null && !price.isEmpty()) {
                        JOptionPane.showMessageDialog(detailsTable, "Seat: " + seatNo + "\nPrice: " + price,
                                "Seat Rate", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        JScrollPane detailsScroll = new JScrollPane(detailsTable);
        eventOverviewPanel.add(detailsScroll, BorderLayout.CENTER);

        // Load events from DB
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT event_id, event_name, date, stadium FROM event")) {
            while (rs.next()) {
                String eventInfo = rs.getInt("event_id") + ": " + rs.getString("event_name") + " | "
                        + rs.getString("date") + " | " + rs.getString("stadium");
                eventListModel.addElement(eventInfo);
            }
        } catch (Exception ex) {
            eventListModel.addElement("Error loading events: " + ex.getMessage());
        }

        // Load details for selected event
        eventList.addListSelectionListener(e -> {
            detailsModel.setRowCount(0);
            int idx = eventList.getSelectedIndex();
            if (idx < 0)
                return;
            String selected = eventListModel.get(idx);
            String eventId = selected.split(":")[0];
            try (Connection con = DBUtil.getConnection()) {
                // Get all seats, prices, and stadiums
                PreparedStatement psSeats = con.prepareStatement(
                        "SELECT s.stadium_name, seats.seat_no, seats.price FROM seats LEFT JOIN stadium s ON seats.stadium_id = s.stadium_id");
                ResultSet rsSeats = psSeats.executeQuery();
                java.util.Map<String, String[]> seatInfo = new java.util.HashMap<>();
                java.util.Set<String> allSeats = new java.util.HashSet<>();
                while (rsSeats.next()) {
                    String stadium = rsSeats.getString("stadium_name");
                    String seatNo = rsSeats.getString("seat_no");
                    String price = rsSeats.getString("price");
                    allSeats.add(seatNo);
                    seatInfo.put(seatNo, new String[] { stadium, price });
                }
                // Get booked seats and details
                PreparedStatement psTickets = con.prepareStatement(
                        "SELECT t.seat_no, t.price, c.name FROM ticket t JOIN customer c ON t.customer_id = c.customer_id WHERE t.event_id = ?");
                psTickets.setInt(1, Integer.parseInt(eventId));
                ResultSet rsTickets = psTickets.executeQuery();
                java.util.Map<String, Object[]> bookedSeats = new java.util.HashMap<>();
                while (rsTickets.next()) {
                    String seatNo = rsTickets.getString("seat_no");
                    String price = rsTickets.getString("price");
                    String customer = rsTickets.getString("name");
                    String stadium = seatInfo.containsKey(seatNo) ? seatInfo.get(seatNo)[0] : "";
                    bookedSeats.put(seatNo, new Object[] { customer, seatNo + " (" + stadium + ")", price, "No" });
                }
                // Display all seats with price and stadium
                for (String seat : allSeats) {
                    String[] info = seatInfo.get(seat);
                    String stadium = info[0];
                    String price = info[1];
                    if (bookedSeats.containsKey(seat)) {
                        detailsModel.addRow(bookedSeats.get(seat));
                    } else {
                        detailsModel.addRow(new Object[] { "", seat + " (" + stadium + ")", price, "Yes" });
                    }
                }
                // Automatically select first available seat and show price
                for (int i = 0; i < detailsModel.getRowCount(); i++) {
                    String available = detailsModel.getValueAt(i, 3).toString();
                    if ("Yes".equals(available)) {
                        detailsTable.setRowSelectionInterval(i, i);
                        String price = detailsModel.getValueAt(i, 2).toString();
                        detailsTable.setValueAt(price, i, 2);
                        break;
                    }
                }
            } catch (Exception ex) {
                detailsModel.addRow(new Object[] { "Error", "", "", ex.getMessage() });
            }
        });

        tabs.addTab("Event Overview", eventOverviewPanel);
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
