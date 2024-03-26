import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateRequestFrame extends JFrame {
    private JTextField submittedByNameField;
    private JTextArea descriptionArea;
    private JComboBox<String> problemComboBox, severityComboBox;
    private JButton submitButton, knowledgeBaseButton;
    private final EmailService emailService; // Instance variable
    private final ServiceRequestHandler requestHandler; // Instance variable

    public CreateRequestFrame() {
        setTitle("Create New Request");
        getContentPane().setBackground(new Color(0, 128, 0)); // Set background color
        initializeComponents(); // Initialize UI components
        pack();
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        emailService = new EmailService(); // Initialize EmailService
        requestHandler = new ServiceRequestHandler(emailService); // Initialize ServiceRequestHandler
    }
    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create text fields and combo boxes
        problemComboBox = new JComboBox<>(new String[]{"Network Connectivity Issue", "Software Installation Problem", "Hardware Malfunction", "Password Reset Request", "Printer not Working", "Email Configuration Issue", "File Access Permission Problem", "Slow System Performance", "Website Access Problem", "Application Crashing", "Data Backup Request", "System Update Request", "Account Lockout Issue"});

        makeComponentOpaque(problemComboBox); // Make components opaque and set colors

        String[] severities = {"Critical", "Major", "Minor"};
        severityComboBox = new JComboBox<>(severities);
        makeComponentOpaque(severityComboBox);

        submittedByNameField = new JTextField(20);
        makeComponentOpaque(submittedByNameField);

        descriptionArea = new JTextArea(5, 20);
        makeComponentOpaque(descriptionArea);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setOpaque(true);
        descriptionScrollPane.getViewport().setOpaque(true);

        submitButton = new JButton("Submit");
        styleButton(submitButton);

        knowledgeBaseButton = new JButton("Knowledge Base / FAQ");
        knowledgeBaseButton.addActionListener(e -> {
            // Create and show the KnowledgeBaseFrame instance here
            KnowledgeBaseFrame knowledgeBaseFrame = new KnowledgeBaseFrame();
            knowledgeBaseFrame.setVisible(true);
        });

        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false); // Use the background color of the content pane

        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add labels, fields, and buttons to the grid panel
        gridPanel.add(new JLabel("Problem:"), gbc);
        gridPanel.add(problemComboBox, gbc);
        gridPanel.add(new JLabel("Severity:"), gbc);
        gridPanel.add(severityComboBox, gbc);
        gridPanel.add(new JLabel("Submitted By:"), gbc);
        gridPanel.add(submittedByNameField, gbc);
        gridPanel.add(new JLabel("Description:"), gbc);
        gridPanel.add(descriptionScrollPane, gbc);

        gridPanel.add(new JLabel("Helpdesk Contact : ids517helpdesk@gmail.com"), gbc);
        gridPanel.add(submitButton, gbc);
        gridPanel.add(knowledgeBaseButton, gbc);

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
        String problem = (String) problemComboBox.getSelectedItem();
        String severity = (String) severityComboBox.getSelectedItem();
        String submittedByName = submittedByNameField.getText(); // Collect submitted by name
        String description = descriptionArea.getText(); // Collect description

        String sql = "INSERT INTO ServiceRequests(Problem, Severity, SubmittedBy, Description) VALUES(?,?,?,?)";
        int generatedId = -1;
        // database connection logic
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, problem);
            pstmt.setString(2, severity);
            pstmt.setString(3, submittedByName);
            pstmt.setString(4, description);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }

            // Send email notification using the existing EmailService instance
            String recipientEmail = "ids517helpdesk@gmail.com";
            String subject = "New Service Request Created";
            String body = "A new service request has been created:\n\n" +
                    "Problem: " + problem + "\n" +
                    "Severity: " + severity + "\n" +
                    "Submitted By: " + submittedByName + "\n" +
                    "Description: " + description;
            emailService.sendEmail(recipientEmail, subject, body);

            // Process pending requests (optional)
            requestHandler.processPendingRequests();

            JOptionPane.showMessageDialog(this, "Your request has been submitted successfully. Your ticket ID is " + generatedId + ". Helpdesk team will reach out to you within 4 hours.");
            this.dispose(); // Close window after successful submission
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to submit the request: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CreateRequestFrame().setVisible(true));
    }
}
