import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TicketPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> cbEvent, cbCustomer, cbAvailableSeats;
    private JTextField tfSeat, tfPrice;
    private JButton btnBook, btnCancel, btnClear;
    private int selectedId = -1;

    public TicketPanel() {
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(
                new String[] { "ID", "Event", "Customer", "Email", "Phone", "Seat", "Price" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel form = new JPanel(new GridLayout(3, 4, 5, 5));
        cbEvent = new JComboBox<>();
        cbCustomer = new JComboBox<>();
        cbAvailableSeats = new JComboBox<>();
        tfSeat = new JTextField();
        tfPrice = new JTextField();
        btnBook = new JButton("Book");
        btnCancel = new JButton("Cancel");
        btnClear = new JButton("Clear");
        JButton btnRefresh = new JButton("Refresh");

        form.add(new JLabel("Event:"));
        form.add(cbEvent);
        form.add(new JLabel("Customer:"));
        form.add(cbCustomer);
        form.add(new JLabel("Available Seats:"));
        form.add(cbAvailableSeats);
        form.add(new JLabel("Seat No:"));
        form.add(tfSeat);
        form.add(new JLabel("Price:"));
        form.add(tfPrice);
        form.add(btnBook);
        form.add(btnCancel);
        form.add(btnClear);
        form.add(btnRefresh);

        add(form, BorderLayout.SOUTH);

        // Load tickets, events, customers
        loadEvents();
        loadCustomers();
        loadTickets();

        cbEvent.addActionListener(e -> loadAvailableSeatsForEvent());
    }

    private void loadEvents() {
        cbEvent.removeAllItems();
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT event_id, event_name FROM event")) {
            while (rs.next()) {
                cbEvent.addItem(rs.getInt("event_id") + ": " + rs.getString("event_name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + ex.getMessage());
        }
    }

    private void loadCustomers() {
        cbCustomer.removeAllItems();
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT customer_id, name FROM customer")) {
            while (rs.next()) {
                cbCustomer.addItem(rs.getInt("customer_id") + ": " + rs.getString("name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage());
        }
    }

    private void loadTickets() {
        tableModel.setRowCount(0);
        String sql = "SELECT t.ticket_id, e.event_name, c.name, c.email, c.phone, t.seat_no, t.price FROM ticket t JOIN event e ON t.event_id=e.event_id JOIN customer c ON t.customer_id=c.customer_id";
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt(1), // ticket_id
                        rs.getString(2), // event_name
                        rs.getString(3), // customer name
                        rs.getString(4), // email
                        rs.getString(5), // phone
                        rs.getString(6), // seat_no
                        rs.getDouble(7) // price
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading tickets: " + ex.getMessage());
        }
    }

    private int getSelectedEventId() {
        String item = (String) cbEvent.getSelectedItem();
        if (item == null)
            return -1;
        return Integer.parseInt(item.split(":")[0]);
    }

    private int getSelectedCustomerId() {
        String item = (String) cbCustomer.getSelectedItem();
        if (item == null)
            return -1;
        return Integer.parseInt(item.split(":")[0]);
    }

    private void bookTicket() {
        int eventId = getSelectedEventId();
        int customerId = getSelectedCustomerId();
        String seat = tfSeat.getText();
        String priceStr = tfPrice.getText();
        if (eventId == -1 || customerId == -1 || seat.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required.");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid price.");
            return;
        }
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO ticket (event_id, customer_id, seat_no, price) VALUES (?, ?, ?, ?)");) {
            ps.setInt(1, eventId);
            ps.setInt(2, customerId);
            ps.setString(3, seat);
            ps.setDouble(4, price);
            ps.executeUpdate();
            loadTickets();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error booking ticket: " + ex.getMessage());
        }
    }

    private void cancelTicket() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a ticket to cancel.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel selected ticket?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DBUtil.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM ticket WHERE ticket_id=?")) {
                ps.setInt(1, selectedId);
                ps.executeUpdate();
                loadTickets();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error canceling ticket: " + ex.getMessage());
            }
        }
    }

    private void clearForm() {
        cbEvent.setSelectedIndex(-1);
        cbCustomer.setSelectedIndex(-1);
        tfSeat.setText("");
        tfPrice.setText("");
        selectedId = -1;
        table.clearSelection();
    }

    private void loadAvailableSeatsForEvent() {
        cbAvailableSeats.removeAllItems();
        int eventId = getSelectedEventId();
        if (eventId == -1)
            return;
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement()) {
            // Get all seats
            ResultSet rsAll = st.executeQuery("SELECT seat_no FROM seats");
            java.util.List<String> allSeats = new java.util.ArrayList<>();
            while (rsAll.next()) {
                allSeats.add(rsAll.getString("seat_no"));
            }
            // Get booked seats for this event
            ResultSet rsBooked = st.executeQuery("SELECT seat_no FROM ticket WHERE event_id=" + eventId);
            java.util.Set<String> bookedSeats = new java.util.HashSet<>();
            while (rsBooked.next()) {
                bookedSeats.add(rsBooked.getString("seat_no"));
            }
            // Add only available seats
            for (String seat : allSeats) {
                if (!bookedSeats.contains(seat)) {
                    cbAvailableSeats.addItem(seat);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading available seats: " + ex.getMessage());
        }
    }
}
