import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null); // Center the window on the screen

        setLayout(new GridLayout(3, 2, 5, 5));

        initializeComponents();
    }

    private void initializeComponents() {
        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Here you would open the registration frame
                RegistrationFrame registrationFrame = new RegistrationFrame();
                registrationFrame.setVisible(true);
            }
        });

        pack(); // Adjusts window size to fit components
    }

    private void performLogin() {
        // Example validation (replace with real validation logic)
        if (verifyLogin(emailField.getText(), new String(passwordField.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false); // Hide LoginFrame
            DashboardFrame dashboardFrame = new DashboardFrame(); // Initialize Dashboard
            dashboardFrame.setVisible(true); // Show DashboardFrame
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean verifyLogin(String email, String password) {
        // Example database connection and query (ensure you handle exceptions appropriately)
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?"; // Simplified; passwords should be hashed and checked securely

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // User found, credentials are valid
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Handle exception (e.g., logging, user feedback)
        }
        // User not found or credentials are invalid
        return false;
    }
}
