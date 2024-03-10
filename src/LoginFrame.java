import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        setTitle("ResolveIT - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout(5, 5)); // Use BorderLayout for overall frame layout

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(3, 2, 10, 10)); // GridLayout for the fields and buttons
        fieldsPanel.setBackground(Color.WHITE); // Set background color of fields panel

        initializeComponents(fieldsPanel); // Pass the fields panel for adding components

        add(fieldsPanel, BorderLayout.CENTER); // Add the fields panel to the center

        pack(); // Adjusts window size to fit components
        setSize(600, 300); // Ensure the window has a fixed size
    }

    private void initializeComponents(JPanel panel) {
        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Center align the text fields
        emailField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setHorizontalAlignment(JTextField.CENTER);

        // Adding components to the panel
        panel.add(new JLabel("Email:", SwingConstants.CENTER));
        panel.add(emailField);
        panel.add(new JLabel("Password:", SwingConstants.CENTER));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        // Set the background of buttons and adjust their color
        Color greenColor = new Color(0, 128, 0);
        loginButton.setBackground(greenColor);
        registerButton.setBackground(greenColor);
        loginButton.setForeground(Color.WHITE);
        registerButton.setForeground(Color.WHITE);

        // Making buttons opaque to show the background color
        loginButton.setOpaque(true);
        registerButton.setOpaque(true);
        loginButton.setBorderPainted(false); // Optional: remove the button border
        registerButton.setBorderPainted(false);

        // Add action listeners
        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> {
            RegistrationFrame registrationFrame = new RegistrationFrame();
            registrationFrame.setVisible(true);
            LoginFrame.this.setVisible(false); // Hide the login frame
        });
    }

    private void performLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (verifyLogin(email, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Close LoginFrame
            DashboardFrame dashboardFrame = new DashboardFrame(); // Initialize Dashboard
            dashboardFrame.setVisible(true); // Show DashboardFrame
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean verifyLogin(String email, String password) {
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // User found, credentials are valid
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // User not found or credentials are invalid
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
