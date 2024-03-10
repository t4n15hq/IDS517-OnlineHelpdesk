import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewRequestsFrame extends JFrame {
    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JButton supervisorLoginButton;

    public ViewRequestsFrame() {
        setTitle("View Requests");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 128, 0)); // Green background for the frame
        setLayout(new BorderLayout());
        initializeTable();
        loadDataFromDatabase();
        initializeSupervisorLoginButton();
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    private void initializeTable() {
        String[] columnNames = {"ID", "Title", "Priority", "Severity", "Description", "Status", "Submitted By", "Assigned To"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        requestsTable = new JTable(tableModel);
        styleTable();
        JScrollPane scrollPane = new JScrollPane(requestsTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleTable() {
        requestsTable.setFillsViewportHeight(true);
        requestsTable.setBackground(Color.WHITE);
        requestsTable.setForeground(Color.BLACK); // Set the text inside the table to black
        requestsTable.setGridColor(new Color(0, 128, 0));
        JTableHeader header = requestsTable.getTableHeader();
        header.setBackground(new Color(0, 128, 0));
        header.setForeground(Color.WHITE);
    }

    private void loadDataFromDatabase() {
        tableModel.setRowCount(0);

        String sql = "SELECT RequestID, Title, Priority, Severity, Description, Status, SubmittedBy FROM ServiceRequests";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("RequestID"),
                        rs.getString("Title"),
                        rs.getString("Priority"),
                        rs.getString("Severity"),
                        rs.getString("Description"),
                        rs.getString("Status"),
                        rs.getString("SubmittedBy"),
                        "IT Helpdesk"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeSupervisorLoginButton() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 128, 0)); // Match the frame background
        supervisorLoginButton = new JButton("Supervisor Login");
        supervisorLoginButton.addActionListener(e -> openManagerApprovalFrame());
        buttonPanel.add(supervisorLoginButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void openManagerApprovalFrame() {
        ManagerApprovalFrame managerApprovalFrame = new ManagerApprovalFrame();
        managerApprovalFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewRequestsFrame::new);
    }
}
