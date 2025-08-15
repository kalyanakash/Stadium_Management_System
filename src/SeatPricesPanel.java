import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SeatPricesPanel extends JPanel {
    public SeatPricesPanel() {
        setLayout(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[] { "Stadium", "Seat No", "Price" }, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load seat prices from DB
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(
                        "SELECT s.stadium_name, seats.seat_no, seats.price FROM seats LEFT JOIN stadium s ON seats.stadium_id = s.stadium_id ORDER BY s.stadium_name, seats.seat_no")) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                String stadium = rs.getString("stadium_name");
                String seatNo = rs.getString("seat_no");
                String price = rs.getString("price");
                if (stadium == null)
                    stadium = "Unknown";
                if (price == null)
                    price = "Not Set";
                tableModel.addRow(new Object[] { stadium, seatNo, price });
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "No seat prices found in the database.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading seat prices: " + ex.getMessage());
        }
    }
}
