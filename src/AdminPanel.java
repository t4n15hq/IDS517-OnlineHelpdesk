import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AdminPanel extends JFrame {
    private JTable usersTable;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private DefaultTableModel tableModel;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        usersTable = new JTable();
        tableModel = new DefaultTableModel(new String[]{"UserID", "Name", "Email"}, 0);
        usersTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");

        addButton.addActionListener(this::addNewUser);
        editButton.addActionListener(this::editSelectedUser);
        deleteButton.addActionListener(this::deleteSelectedUser);
        refreshButton.addActionListener(e -> loadUsersData());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadUsersData();
    }

    private void loadUsersData() {
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        model.setRowCount(0); // Clear existing data
        String sql = "SELECT UserID, Name, Email FROM Users";
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("UserID"),
                        rs.getString("Name"),
                        rs.getString("Email")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
                "Password:", passwordField // Add a password field to the input dialog
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add New User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO Users (Name, Email, Role, Password) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseHelper.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, emailField.getText());
                pstmt.setString(3, roleField.getText());
                pstmt.setString(4, new String(passwordField.getPassword())); // Include the password
                pstmt.executeUpdate();
                loadUsersData(); // Refresh data
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void editSelectedUser(ActionEvent e) {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
            return;
        }
        Object userID = usersTable.getValueAt(selectedRow, 0);
        JTextField nameField = new JTextField(usersTable.getValueAt(selectedRow, 1).toString());
        JTextField emailField = new JTextField(usersTable.getValueAt(selectedRow, 2).toString());
        Object[] message = {
                "Name:", nameField,
                "Email:", emailField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String sql = "UPDATE Users SET Name = ?, Email = ? WHERE UserID = ?";
            try (Connection conn = DatabaseHelper.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, emailField.getText());
                pstmt.setInt(3, (Integer) userID);
                pstmt.executeUpdate();
                loadUsersData(); // Refresh data
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteSelectedUser(ActionEvent e) {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0) {
            Object userID = usersTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this user?", "Delete User", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM Users WHERE UserID = ?";
                try (Connection conn = DatabaseHelper.connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, (Integer) userID);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        loadUsersData();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel().setVisible(true));
    }}
