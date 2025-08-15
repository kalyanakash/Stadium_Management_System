
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField tfName, tfEmail, tfPhone;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private int selectedId = -1;

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(new String[] { "ID", "Name", "Email", "Phone" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel form = new JPanel(new GridLayout(2, 4, 5, 5));
        tfName = new JTextField();
        tfEmail = new JTextField();
        tfPhone = new JTextField();
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        form.add(new JLabel("Name:"));
        form.add(tfName);
        form.add(new JLabel("Email:"));
        form.add(tfEmail);
        form.add(new JLabel("Phone:"));
        form.add(tfPhone);
        form.add(btnAdd);
        form.add(btnUpdate);
        form.add(btnDelete);
        form.add(btnClear);

        add(form, BorderLayout.SOUTH);

        // Load customers
        loadCustomers();

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

    private void loadCustomers() {
        tableModel.setRowCount(0);
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM customer")) {
            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("customer_id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("phone") });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage());
        }
    }

    private void addCustomer() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required.");
            return;
        }
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con
                        .prepareStatement("INSERT INTO customer (name, email, phone) VALUES (?, ?, ?)");) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
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
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con
                        .prepareStatement("UPDATE customer SET name=?, email=?, phone=? WHERE customer_id=?")) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, selectedId);
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
