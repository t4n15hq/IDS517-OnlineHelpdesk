import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerApprovalFrame extends JFrame {
    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JButton approveButton, rejectButton;
    private JTextArea commentArea;

    public ManagerApprovalFrame() {
        setTitle("Approve Requests");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600); // Adjust size as needed
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize UI components
        initializeUI();

        // Load service requests from the database
        loadDataFromDatabase();
    }

    private void initializeUI() {
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Priority", "Severity", "Description", "Status"}, 0);
        requestsTable = new JTable(tableModel);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        commentArea = new JTextArea(5, 30);
        approveButton = new JButton("Approve");
        rejectButton = new JButton("Reject");

        approveButton.addActionListener(this::approveRequest);
        rejectButton.addActionListener(this::rejectRequest);

        bottomPanel.add(new JScrollPane(commentArea));
        bottomPanel.add(approveButton);
        bottomPanel.add(rejectButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadDataFromDatabase() {
        tableModel.setRowCount(0); // Clear existing data
        String sql = "SELECT RequestID, Title, Priority, Severity, Description, Status FROM ServiceRequests";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {rs.getInt("RequestID"), rs.getString("Title"), rs.getString("Priority"),
                        rs.getString("Severity"), rs.getString("Description"), rs.getString("Status")};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void approveRequest(ActionEvent event) {
        updateRequestStatus(true);
    }

    private void rejectRequest(ActionEvent event) {
        updateRequestStatus(false);
    }

    private void updateRequestStatus(boolean isApproved) {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request to approve or reject.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = isApproved ? "Approved" : "Rejected";
        String comment = commentArea.getText();
        int requestId = (Integer) tableModel.getValueAt(selectedRow, 0); // Assuming ID is the first column

        String sql = "UPDATE ServiceRequests SET Status = ?, Comment = ? WHERE RequestID = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, comment);
            pstmt.setInt(3, requestId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Request " + status.toLowerCase() + " successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadDataFromDatabase(); // Refresh data
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Example usage
        SwingUtilities.invokeLater(() -> {
            new ManagerApprovalFrame().setVisible(true);
        });
    }
}
