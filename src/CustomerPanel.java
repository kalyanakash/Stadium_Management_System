
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField tfName, tfEmail, tfPhone;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JComboBox<String> cbSeats;
    private int selectedId = -1;

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(new String[] { "ID", "Name", "Email", "Phone" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel form = new JPanel(new GridLayout(2, 5, 5, 5));
        tfName = new JTextField();
        tfEmail = new JTextField();
        tfPhone = new JTextField();
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        cbSeats = new JComboBox<>();
        cbSeats.setEnabled(true); // Allow selection

        form.add(new JLabel("Name:"));
        form.add(tfName);
        form.add(new JLabel("Email:"));
        form.add(tfEmail);
        form.add(new JLabel("Phone:"));
        form.add(tfPhone);
        form.add(new JLabel("Available Seats:"));
        form.add(cbSeats);
        form.add(btnAdd);
        form.add(btnUpdate);
        form.add(btnDelete);
        form.add(btnClear);

        add(form, BorderLayout.SOUTH);

        // Load customers
        loadCustomers();
        loadSeats();

        // Table selection
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                tfName.setText(tableModel.getValueAt(row, 1).toString());
                tfEmail.setText(tableModel.getValueAt(row, 2).toString());
                tfPhone.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearForm());
    }

    private void loadSeats() {
        cbSeats.removeAllItems();
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rsAll = st.executeQuery(
                        "SELECT s.stadium_name, seat_no, price FROM seats LEFT JOIN stadium s ON seats.stadium_id = s.stadium_id")) {
            // Collect all seats with stadium and price
            java.util.List<String[]> allSeats = new java.util.ArrayList<>();
            while (rsAll.next()) {
                String stadium = rsAll.getString("stadium_name");
                String seatNo = rsAll.getString("seat_no");
                String price = rsAll.getString("price");
                allSeats.add(new String[] { seatNo, stadium, price });
            }
            // Find booked seats
            ResultSet rsBooked = st.executeQuery("SELECT seat_no FROM ticket");
            java.util.Set<String> bookedSeats = new java.util.HashSet<>();
            while (rsBooked.next()) {
                bookedSeats.add(rsBooked.getString("seat_no"));
            }
            // Display seats with status, stadium, and price
            for (String[] seatArr : allSeats) {
                String seat = seatArr[0];
                String stadium = seatArr[1];
                String price = seatArr[2];
                if (bookedSeats.contains(seat)) {
                    cbSeats.addItem(seat + " (" + stadium + ") - Rs. " + price + " (Booked)");
                } else {
                    cbSeats.addItem(seat + " (" + stadium + ") - Rs. " + price + " (Available)");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading seats: " + ex.getMessage());
        }
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM customer")) {
            java.util.Set<String> uniqueCustomers = new java.util.HashSet<>();
            while (rs.next()) {
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String uniqueKey = email + "|" + phone;
                if (!uniqueCustomers.contains(uniqueKey)) {
                    tableModel.addRow(new Object[] { rs.getInt("customer_id"), rs.getString("name"), email, phone });
                    uniqueCustomers.add(uniqueKey);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage());
        }
    }

    private void addCustomer() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String seat = (String) cbSeats.getSelectedItem();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || seat == null || seat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields including seat are required.");
            return;
        }
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con
                        .prepareStatement("INSERT INTO customer (name, email, phone) VALUES (?, ?, ?)");) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            // Optionally, you can store seat info in customer table if schema allows
            ps.executeUpdate();
            loadCustomers();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding customer: " + ex.getMessage());
        }
    }

    private void updateCustomer() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer to update.");
            return;
        }
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String seat = (String) cbSeats.getSelectedItem();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || seat == null || seat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields including seat are required.");
            return;
        }
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con
                        .prepareStatement("UPDATE customer SET name=?, email=?, phone=? WHERE customer_id=?")) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, selectedId);
            // Optionally, you can store seat info in customer table if schema allows
            ps.executeUpdate();
            loadCustomers();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating customer: " + ex.getMessage());
        }
    }

    private void deleteCustomer() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected customer?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DBUtil.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM customer WHERE customer_id=?")) {
                ps.setInt(1, selectedId);
                ps.executeUpdate();
                loadCustomers();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting customer: " + ex.getMessage());
            }
        }
    }

    private void clearForm() {
        tfName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        selectedId = -1;
        table.clearSelection();
    }
}
