import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateRequestFrame extends JFrame {
    private JTextField titleField, submittedByNameField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityComboBox, severityComboBox;
    private JButton submitButton;

    public CreateRequestFrame() {
        setTitle("Create New Request");
        getContentPane().setBackground(new Color(0, 128, 0)); // Set background color
        initializeComponents(); // Initialize UI components
        pack();
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create text fields and combo boxes
        titleField = new JTextField(20);
        makeComponentOpaque(titleField); // Make components opaque and set colors

        descriptionArea = new JTextArea(5, 20);
        makeComponentOpaque(descriptionArea);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setOpaque(true);
        descriptionScrollPane.getViewport().setOpaque(true);

        String[] priorities = {"High", "Medium", "Low"};
        priorityComboBox = new JComboBox<>(priorities);
        makeComponentOpaque(priorityComboBox);

        String[] severities = {"Critical", "Major", "Minor"};
        severityComboBox = new JComboBox<>(severities);
        makeComponentOpaque(severityComboBox);

        submittedByNameField = new JTextField(20);
        makeComponentOpaque(submittedByNameField);

        submitButton = new JButton("Submit");
        styleButton(submitButton);

        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false); // Use the background color of the content pane

        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add labels, fields, and button to the grid panel
        gridPanel.add(new JLabel("Title:"), gbc);
        gridPanel.add(titleField, gbc);
        gridPanel.add(new JLabel("Description:"), gbc);
        gridPanel.add(descriptionScrollPane, gbc);
        gridPanel.add(new JLabel("Priority:"), gbc);
        gridPanel.add(priorityComboBox, gbc);
        gridPanel.add(new JLabel("Severity:"), gbc);
        gridPanel.add(severityComboBox, gbc);
        gridPanel.add(new JLabel("Submitted By:"), gbc);
        gridPanel.add(submittedByNameField, gbc);
        gridPanel.add(submitButton, gbc);

        // Attach action listener to the submit button
        submitButton.addActionListener(this::submitRequest);
        getContentPane().add(gridPanel); // Add gridPanel to the frame's content pane
    }

    // Method to make components opaque and set colors
    private void makeComponentOpaque(JComponent component) {
        component.setOpaque(true);
        component.setBackground(Color.WHITE); // Set background to white
        component.setForeground(new Color(0, 0, 0)); // Set text color to black
    }

    // Method to style the submit button
    private void styleButton(JButton button) {
        button.setOpaque(true);
        button.setBackground(new Color(0, 0, 0)); // Set background to green
        button.setForeground(Color.WHITE); // Set text color to white
        button.setBorderPainted(false); // Disable border painting
    }

    // Method to handle request submission
    private void submitRequest(ActionEvent e) {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String priority = (String) priorityComboBox.getSelectedItem();
        String severity = (String) severityComboBox.getSelectedItem();
        String submittedByName = submittedByNameField.getText(); // Collect submitted by name

        String sql = "INSERT INTO ServiceRequests(Title, Description, Priority, Severity, SubmittedBy) VALUES(?,?,?,?,?)";

        // database connection logic
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, priority);
            pstmt.setString(4, severity);
            pstmt.setString(5, submittedByName);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Request submitted successfully!");
            this.dispose(); // Close window after successful submission
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to submit the request: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CreateRequestFrame().setVisible(true));
    }
}
