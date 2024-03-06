import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewRequestsFrame extends JFrame {
    private JTable requestsTable;
    private DefaultTableModel tableModel;

    public ViewRequestsFrame() {
        // Setup frame
        initializeTable();
        setSize(400, 300);
        loadDataFromDatabase();
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Priority", "Severity", "Description"}, 0);
        requestsTable = new JTable(tableModel);
        add(new JScrollPane(requestsTable), BorderLayout.CENTER);
    }

    private void loadDataFromDatabase() {
        String sql = "SELECT RequestID, Title, Priority, Severity, Description FROM ServiceRequests";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getInt("RequestID"),
                        rs.getString("Title"),
                        rs.getString("Priority"),
                        rs.getString("Severity"),
                        rs.getString("Description")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
