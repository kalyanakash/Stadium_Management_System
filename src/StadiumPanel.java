import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StadiumPanel extends JPanel {
    public StadiumPanel() {
        setLayout(new BorderLayout());
        DefaultTableModel stadiumModel = new DefaultTableModel(new String[] { "Stadium Name", "Location" }, 0);
        JTable stadiumTable = new JTable(stadiumModel);
        JScrollPane scrollPane = new JScrollPane(stadiumTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load stadiums from DB
        try (Connection con = DBUtil.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT stadium_name, location FROM stadium")) {
            while (rs.next()) {
                String name = rs.getString("stadium_name");
                String location = rs.getString("location");
                stadiumModel.addRow(new Object[] { name, location });
            }
        } catch (Exception ex) {
            stadiumModel.addRow(new Object[] { "Error loading stadiums", ex.getMessage() });
        }
    }
}
