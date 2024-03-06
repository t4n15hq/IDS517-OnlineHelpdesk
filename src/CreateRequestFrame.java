import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateRequestFrame extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityComboBox;
    private JComboBox<String> severityComboBox;
    private JButton submitButton;

    public CreateRequestFrame() {
        setTitle("Create New Request");
        setSize(400, 300); // Adjust size as needed
        setLayout(new GridBagLayout()); // Using GridBagLayout for flexibility
        initializeComponents();
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea); // Make the TextArea scrollable
        priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        severityComboBox = new JComboBox<>(new String[]{"Critical", "Major", "Minor"});
        submitButton = new JButton("Submit");

        add(new JLabel("Title:"), gbc);
        add(titleField, gbc);
        add(new JLabel("Description:"), gbc);
        add(scrollPane, gbc);
        add(new JLabel("Priority:"), gbc);
        add(priorityComboBox, gbc);
        add(new JLabel("Severity:"), gbc);
        add(severityComboBox, gbc);
        add(submitButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRequest();
            }
        });
    }

    private void submitRequest() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String priority = (String) priorityComboBox.getSelectedItem();
        String severity = (String) severityComboBox.getSelectedItem();

        // Use your database helper to connect and insert the request
        // Replace "DatabaseHelper.connect()" with your actual database connection method
        String sql = "INSERT INTO ServiceRequests(Title, Description, Priority, Severity) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseHelper.connect(); // TODO: Provide actual database connection
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, priority);
            pstmt.setString(4, severity);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Request submitted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to submit the request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
}
