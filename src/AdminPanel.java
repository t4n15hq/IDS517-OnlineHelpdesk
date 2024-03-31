import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AdminPanel extends JFrame {
    private JTable usersTable, serviceRequestsTable;
    private DefaultTableModel usersTableModel, serviceRequestsTableModel;
    private JButton addUserButton, editUserButton, deleteUserButton, refreshUsersButton;
    private JButton addRequestButton, editRequestButton, deleteRequestButton, refreshRequestsButton;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Users", createUserTab());
        tabbedPane.addTab("Service Requests", createServiceRequestsTab());
        add(tabbedPane, BorderLayout.CENTER);
        loadUsersData();
        loadServiceRequestsData();
    }

    private JPanel createUserTab() {
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersTableModel = new DefaultTableModel(new String[]{"UserID", "Name", "Email", "Role"}, 0);
        usersTable = new JTable(usersTableModel);
        usersPanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        addUserButton = new JButton("Add User");
        editUserButton = new JButton("Edit User");
        deleteUserButton = new JButton("Delete User");
        refreshUsersButton = new JButton("Refresh Users");
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(refreshUsersButton);
        usersPanel.add(buttonPanel, BorderLayout.SOUTH);
        addUserButton.addActionListener(this::addNewUser);
        editUserButton.addActionListener(this::editSelectedUser);
        deleteUserButton.addActionListener(this::deleteSelectedUser);
        refreshUsersButton.addActionListener(e -> loadUsersData());
        return usersPanel;
    }

    private JPanel createServiceRequestsTab() {
        JPanel requestsPanel = new JPanel(new BorderLayout());
        serviceRequestsTableModel = new DefaultTableModel(new String[]{"RequestID", "Problem","Priority", "Severity","SubmittedBy", "Description", "Status"}, 0);
        serviceRequestsTable = new JTable(serviceRequestsTableModel);
        requestsPanel.add(new JScrollPane(serviceRequestsTable), BorderLayout.CENTER);
        JPanel requestsButtonPanel = new JPanel();
        addRequestButton = new JButton("Add Request");
        editRequestButton = new JButton("Edit Request");
        deleteRequestButton = new JButton("Delete Request");
        refreshRequestsButton = new JButton("Refresh Requests");
        requestsButtonPanel.add(addRequestButton);
        requestsButtonPanel.add(editRequestButton);
        requestsButtonPanel.add(deleteRequestButton);
        requestsButtonPanel.add(refreshRequestsButton);
        requestsPanel.add(requestsButtonPanel, BorderLayout.SOUTH);
        addRequestButton.addActionListener(this::addNewRequest);
        editRequestButton.addActionListener(this::editSelectedRequest);
        deleteRequestButton.addActionListener(this::deleteSelectedRequest);
        refreshRequestsButton.addActionListener(e -> loadServiceRequestsData());
        return requestsPanel;
    }

    // Methods for User Operations
    private void loadUsersData() {
        usersTableModel.setRowCount(0); // Clear current table content
        try (Connection connection = DatabaseHelper.connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT UserID, Name, Email, Role FROM Users")) {
            while (rs.next()) {
                usersTableModel.addRow(new Object[]{rs.getInt("UserID"), rs.getString("Name"), rs.getString("Email"), rs.getString("Role")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewUser(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField roleField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Role:", roleField,
                "Password:", passwordField
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Add New User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String insertSql = "INSERT INTO Users (Name, Email, Role, Password) VALUES (?, ?, ?, ?)";
            try (Connection connection = DatabaseHelper.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                preparedStatement.setString(1, nameField.getText());
                preparedStatement.setString(2, emailField.getText());
                preparedStatement.setString(3, roleField.getText());
                preparedStatement.setString(4, new String(passwordField.getPassword())); // Note: Password should be securely hashed in real applications
                preparedStatement.executeUpdate();
                loadUsersData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelectedUser(ActionEvent e) {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Extract user details from the selected row
        Integer userID = (Integer) usersTable.getValueAt(selectedRow, 0);
        String currentName = (String) usersTable.getValueAt(selectedRow, 1);
        String currentEmail = (String) usersTable.getValueAt(selectedRow, 2);
        String currentRole = (String) usersTable.getValueAt(selectedRow, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField roleField = new JTextField(currentRole);
        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Role:", roleField
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String updateSql = "UPDATE Users SET Name = ?, Email = ?, Role = ? WHERE UserID = ?";
            try (Connection connection = DatabaseHelper.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
                preparedStatement.setString(1, nameField.getText());
                preparedStatement.setString(2, emailField.getText());
                preparedStatement.setString(3, roleField.getText());
                preparedStatement.setInt(4, userID);
                preparedStatement.executeUpdate();
                loadUsersData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteSelectedUser(ActionEvent e) {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer userID = (Integer) usersTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Second confirmation
            int confirmAgain = JOptionPane.showConfirmDialog(this, "This action cannot be undone. Are you absolutely sure?", "Confirm Delete Again", JOptionPane.YES_NO_OPTION);
            if (confirmAgain == JOptionPane.YES_OPTION) {
                String deleteSql = "DELETE FROM Users WHERE UserID = ?";
                try (Connection connection = DatabaseHelper.connect();
                     PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
                    preparedStatement.setInt(1, userID);
                    preparedStatement.executeUpdate();
                    loadUsersData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    // Methods for Service Request Operations
    private void loadServiceRequestsData() {
        serviceRequestsTableModel.setRowCount(0);
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT RequestID, Problem,Priority, Severity,SubmittedBy, Description, Status FROM ServiceRequests")) {
            while (rs.next()) {
                serviceRequestsTableModel.addRow(new Object[]{
                        rs.getInt("RequestID"),
                        rs.getString("Problem"),
                        rs.getString("Priority"),
                        rs.getString("Severity"),
                        rs.getString("SubmittedBy"),
                        rs.getString("Description"),
                        rs.getString("Status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading service requests: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewRequest(ActionEvent e) {
        JTextField problemField = new JTextField();
        JTextField severityField = new JTextField();
        JTextArea descriptionArea = new JTextArea(5, 20);
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Open", "In Progress", "Resolved", "Closed"});

        Object[] message = {
                "Problem:", problemField,
                "Severity:", severityField,
                "Description:", new JScrollPane(descriptionArea),
                "Status:", statusComboBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add New Service Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO ServiceRequests (Problem, Severity, Description, Status, SubmittedBy) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseHelper.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, problemField.getText());
                pstmt.setString(2, severityField.getText());
                pstmt.setString(3, descriptionArea.getText());
                pstmt.setString(4, (String) statusComboBox.getSelectedItem());
                pstmt.setInt(5, 1);  // Replace 1 with the ID of the logged-in user
                pstmt.executeUpdate();
                loadServiceRequestsData(); // Refresh the displayed data
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding service request: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void editSelectedRequest(ActionEvent e) {
        int selectedRow = serviceRequestsTable.getSelectedRow();
        if (selectedRow >= 0) {
            Object requestID = serviceRequestsTable.getValueAt(selectedRow, 0);
            JTextField problemField = new JTextField(serviceRequestsTable.getValueAt(selectedRow, 1).toString());
            JTextField severityField = new JTextField(serviceRequestsTable.getValueAt(selectedRow, 2).toString());
            JTextArea descriptionArea = new JTextArea(serviceRequestsTable.getValueAt(selectedRow, 3).toString(), 5, 20);
            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Open", "In Progress", "Resolved", "Closed"});
            statusComboBox.setSelectedItem(serviceRequestsTable.getValueAt(selectedRow, 4));

            Object[] message = {
                    "Problem:", problemField,
                    "Severity:", severityField,
                    "Description:", new JScrollPane(descriptionArea),
                    "Status:", statusComboBox
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Edit Service Request", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String sql = "UPDATE ServiceRequests SET Problem = ?, Severity = ?, Description = ?, Status = ? WHERE RequestID = ?";
                try (Connection conn = DatabaseHelper.connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, problemField.getText());
                    pstmt.setString(2, severityField.getText());
                    pstmt.setString(3, descriptionArea.getText());
                    pstmt.setString(4, (String)statusComboBox.getSelectedItem());
                    pstmt.setInt(5, (Integer)requestID);
                    pstmt.executeUpdate();
                    loadServiceRequestsData(); // Refresh the displayed data
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error editing service request: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a service request to edit.");
        }
    }


    private void deleteSelectedRequest(ActionEvent e) {
        int selectedRow = serviceRequestsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a service request to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object requestID = serviceRequestsTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this service request?", "Delete Service Request", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Second confirmation
            int confirmAgain = JOptionPane.showConfirmDialog(this, "This action cannot be undone. Are you absolutely sure?", "Confirm Delete Again", JOptionPane.YES_NO_OPTION);
            if (confirmAgain == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM ServiceRequests WHERE RequestID = ?";
                try (Connection conn = DatabaseHelper.connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, (Integer)requestID);
                    pstmt.executeUpdate();
                    loadServiceRequestsData(); // Refresh the displayed data
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting service request: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel().setVisible(true));
    }
}
