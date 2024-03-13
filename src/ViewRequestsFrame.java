import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ViewRequestsFrame extends JFrame {
    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JButton supervisorLoginButton;
    private JTextField searchField;
    private JButton searchButton;

    public ViewRequestsFrame() {
        setTitle("View Requests");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 128, 0));
        setLayout(new BorderLayout());
        initializeSearchComponents();
        initializeTable();
        loadDataFromDatabase();
        initializeSupervisorLoginButton();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeSearchComponents() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Ticket ID:"));

        searchField = new JTextField(15);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchTicketById());
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);
    }

    private void initializeTable() {
        String[] columnNames = {
                "RequestID", "Problem", "Priority", "Severity", "Description", "Status", "Submitted By", "Assigned To", "Request Raised", "Resolution Date"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        requestsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestsTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadDataFromDatabase() {
        tableModel.setRowCount(0); // Clear existing data
        String sql = "SELECT RequestID, Problem, Priority, Severity, Description, Status, SubmittedBy, Timestamp, ResolutionDate FROM ServiceRequests";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("RequestID"),
                        rs.getString("Problem"),
                        rs.getString("Priority"),
                        rs.getString("Severity"),
                        rs.getString("Description"),
                        rs.getString("Status"),
                        rs.getString("SubmittedBy"),
                        "IT Helpdesk", // Placeholder, you might need to adjust this
                        rs.getTimestamp("Timestamp"),
                        rs.getDate("ResolutionDate")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchTicketById() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadDataFromDatabase(); // If search field is empty, load all data
            return;
        }

        try {
            int requestId = Integer.parseInt(searchText); // Convert searchText to integer
            tableModel.setRowCount(0); // Clear the table

            String sql = "SELECT RequestID, Problem, Priority, Severity, Description, Status, SubmittedBy, Timestamp, ResolutionDate FROM ServiceRequests WHERE RequestID = ?";
            try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, requestId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("RequestID"),
                            rs.getString("Problem"),
                            rs.getString("Priority"),
                            rs.getString("Severity"),
                            rs.getString("Description"),
                            rs.getString("Status"),
                            rs.getString("SubmittedBy"),
                            "IT Helpdesk",
                            rs.getTimestamp("Timestamp"),
                            rs.getDate("ResolutionDate")
                    };
                    tableModel.addRow(row);
                }
                if(tableModel.getRowCount() == 0){
                    JOptionPane.showMessageDialog(this, "No records found for Request ID: " + requestId, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Request ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeSupervisorLoginButton() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 128, 0)); // Match the frame background
        supervisorLoginButton = new JButton("Supervisor Login");
        supervisorLoginButton.addActionListener(e -> {
            // Create and show the ManagerApprovalFrame
            ManagerApprovalFrame managerApprovalFrame = new ManagerApprovalFrame();
            managerApprovalFrame.setVisible(true);
            // Optionally, you might want to hide the current frame, or perform other actions
        });
        buttonPanel.add(supervisorLoginButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    private Connection connect() throws SQLException {
        // Ensure this path correctly points to your database
        String url = "jdbc:sqlite:MyHelpdeskDB.db";
        return DriverManager.getConnection(url);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewRequestsFrame::new);
    }
}
