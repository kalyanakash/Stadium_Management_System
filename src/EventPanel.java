import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EventPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField tfName, tfDate, tfStadium, tfSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private int selectedId = -1;

    public EventPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(236, 240, 241));

        // Table setup
        tableModel = new DefaultTableModel(new String[] { "ID", "Name", "Date", "Stadium" }, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setGridColor(new Color(189, 195, 199));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(236, 240, 241));
        tfSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        styleButton(btnSearch);
        searchPanel.add(new JLabel("Search Event:"));
        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBackground(new Color(236, 240, 241));
        tfName = new JTextField();
        tfDate = new JTextField();
        tfStadium = new JTextField();
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        styleButton(btnAdd);
        styleButton(btnUpdate);
        styleButton(btnDelete);
        styleButton(btnClear);

        formPanel.add(new JLabel("Event Name:"));
        formPanel.add(tfName);
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        formPanel.add(tfDate);
        formPanel.add(new JLabel("Stadium Name:"));
        formPanel.add(tfStadium);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);
        formPanel.add(btnClear);

        add(formPanel, BorderLayout.SOUTH);

        // Load events
        loadEvents();

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                tfName.setText(tableModel.getValueAt(row, 1).toString());
                tfDate.setText(tableModel.getValueAt(row, 2).toString());
                tfStadium.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        // Button actions
        btnAdd.addActionListener(e -> addEvent());
        btnUpdate.addActionListener(e -> updateEvent());
        btnDelete.addActionListener(e -> deleteEvent());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchEvents());
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    private void loadEvents() {
        tableModel.setRowCount(0);
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM event")) {
            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("event_id"), rs.getString("event_name"),
                        rs.getString("event_date"), rs.getString("stadium_name") });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + ex.getMessage());
        }
    }

    private void addEvent() {
        String name = tfName.getText();
        String date = tfDate.getText();
        String stadium = tfStadium.getText();
        if (name.isEmpty() || date.isEmpty() || stadium.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required.");
            return;
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.");
            return;
        }
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO event (event_name, event_date, stadium_name) VALUES (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setString(3, stadium);
            ps.executeUpdate();
            loadEvents();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding event: " + ex.getMessage());
        }
    }

    private void updateEvent() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to update.");
            return;
        }
        String name = tfName.getText();
        String date = tfDate.getText();
        String stadium = tfStadium.getText();
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE event SET event_name=?, event_date=?, stadium_name=? WHERE event_id=?")) {
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setString(3, stadium);
            ps.setInt(4, selectedId);
            ps.executeUpdate();
            loadEvents();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating event: " + ex.getMessage());
        }
    }

    private void deleteEvent() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected event?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DBUtil.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM event WHERE event_id=?")) {
                ps.setInt(1, selectedId);
                ps.executeUpdate();
                loadEvents();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting event: " + ex.getMessage());
            }
        }
    }

    private void searchEvents() {
        String keyword = tfSearch.getText().trim();
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM event WHERE event_name LIKE ? OR stadium_name LIKE ?";
        try (Connection con = DBUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getInt("event_id"), rs.getString("event_name"),
                        rs.getString("event_date"), rs.getString("stadium_name") });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching events: " + ex.getMessage());
        }
    }

    private void clearForm() {
        tfName.setText("");
        tfDate.setText("");
        tfStadium.setText("");
        selectedId = -1;
        table.clearSelection();
    }
}
