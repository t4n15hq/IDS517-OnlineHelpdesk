import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerApprovalFrame extends JFrame {
    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JButton approveButton, rejectButton, updatePriorityButton, submitCommentButton;
    private JComboBox<String> priorityComboBox;
    private JTextArea commentArea;

    public ManagerApprovalFrame() {
        setTitle("Supervisor Request Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeUI();
        loadDataFromDatabase();
    }

    private void initializeUI() {
        String[] columnNames = {
                "ID", "Problem", "Severity", "Priority","SubmittedBy", "Description", "Status", "Timestamp", "Resolution Date"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        requestsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        commentArea = new JTextArea(5, 30);
        bottomPanel.add(new JScrollPane(commentArea));

        String[] priorities = {"Priority 1 - Immediate", "Priority 2 - High", "Priority 3 - Medium", "Priority 4 - Low"};
        priorityComboBox = new JComboBox<>(priorities);
        bottomPanel.add(new JLabel("Priority:"));
        bottomPanel.add(priorityComboBox);

        approveButton = new JButton("Mark as Resolved");
        rejectButton = new JButton("Return for Review");
        updatePriorityButton = new JButton("Adjust Priority");
        submitCommentButton = new JButton("Submit Comment");

        approveButton.addActionListener(this::approveRequest);
        rejectButton.addActionListener(this::rejectRequest);
        updatePriorityButton.addActionListener(this::updatePriority);
        submitCommentButton.addActionListener(this::submitComment);

        bottomPanel.add(approveButton);
        bottomPanel.add(rejectButton);
        bottomPanel.add(updatePriorityButton);
        bottomPanel.add(submitCommentButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadDataFromDatabase() {
        tableModel.setRowCount(0);
        String sql = "SELECT RequestID, Problem, Severity, Priority,SubmittedBy, Description, Status, Timestamp, ResolutionDate FROM ServiceRequests";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("RequestID"),
                        rs.getString("Problem"),
                        rs.getString("Severity"),
                        rs.getString("Priority"),
                        rs.getString("SubmittedBy"),
                        rs.getString("Description"),
                        rs.getString("Status"),
                        rs.getTimestamp("Timestamp"),
                        rs.getDate("ResolutionDate")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void approveRequest(ActionEvent event) {
        updateTicketStatus(true, false);
    }

    private void rejectRequest(ActionEvent event) {
        updateTicketStatus(false, false);
    }

    private void updatePriority(ActionEvent event) {
        updateTicketStatus(false, true);
    }

    private void updateTicketStatus(boolean isCompleted, boolean updatePriorityOnly) {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a ticket.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int requestId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String priority = (String) priorityComboBox.getSelectedItem();
        String comment = commentArea.getText();

        try (Connection conn = connect()) {
            String sql;

            if (updatePriorityOnly) {
                // Update priority and comment
                sql = "UPDATE ServiceRequests SET Priority = ?, Comment = ? WHERE RequestID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, priority);
                    pstmt.setString(2, comment); // Update comment here
                    pstmt.setInt(3, requestId);
                    pstmt.executeUpdate();
                }
            } else {
                // Update status, comment, and optionally resolution date
                if (isCompleted) {
                    sql = "UPDATE ServiceRequests SET Status = ?, Comment = ?, ResolutionDate = CURRENT_TIMESTAMP WHERE RequestID = ?";
                } else {
                    sql = "UPDATE ServiceRequests SET Status = ?, Comment = ? WHERE RequestID = ?";
                }
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, isCompleted ? "Resolved" : "Review"); // Use appropriate status
                    pstmt.setString(2, comment); // Update comment here
                    pstmt.setInt(3, requestId);
                    pstmt.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Ticket updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadDataFromDatabase(); // Refresh data to reflect the changes
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitComment(ActionEvent event) {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to submit a comment.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int requestId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String comment = commentArea.getText().trim();

        String sql = "UPDATE ServiceRequests SET Comment = ? WHERE RequestID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, comment);
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Comment submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadDataFromDatabase();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to submit the comment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:MyHelpdeskDB.db";
        return DriverManager.getConnection(url);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerApprovalFrame().setVisible(true));
    }
}
