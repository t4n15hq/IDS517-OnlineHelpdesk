import javax.swing.*;
import java.awt.*;

public class RegistrationFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> roleComboBox;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegistrationFrame() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // Center the window

        setLayout(new GridLayout(5, 2, 5, 5));

        nameField = new JTextField();
        emailField = new JTextField();
        roleComboBox = new JComboBox<>(new String[]{"User", "Helpdesk", "Supervisor"});
        passwordField = new JPasswordField();
        registerButton = new JButton("Register");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Role:"));
        add(roleComboBox);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel()); // Spacer
        add(registerButton);

        registerButton.addActionListener(e -> register());

        pack(); // Adjusts window size to fit components
    }

    private void register() {
        String name = nameField.getText();
        String email = emailField.getText();
        String role = (String) roleComboBox.getSelectedItem();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Assuming DatabaseHelper.executeUpdate returns boolean indicating success/failure
        String sql = "INSERT INTO Users (Name, Email, Role, Password) VALUES (?, ?, ?, ?)";
        boolean success = DatabaseHelper.executeUpdate(sql, new Object[]{name, email, role, password});

        if (success) {
            JOptionPane.showMessageDialog(this, "User registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Optionally close the registration window after successful registration
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
