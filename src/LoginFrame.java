import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton adminButton; // New button for opening Admin Panel.

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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(adminButton);
        add(bottomPanel, BorderLayout.SOUTH); // Add the bottom panel to the south

        pack(); // Adjusts window size to fit components
        setSize(600, 300); // Ensure the window has a fixed size
    }

    private void initializeComponents(JPanel panel) {
        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        adminButton = new JButton("Admin Panel");

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
        adminButton.setBackground(greenColor);
        loginButton.setForeground(Color.WHITE);
        registerButton.setForeground(Color.WHITE);
        adminButton.setForeground(Color.WHITE);

        // Making buttons opaque to show the background color
        loginButton.setOpaque(true);
        registerButton.setOpaque(true);
        adminButton.setOpaque(true);
        loginButton.setBorderPainted(false); // Optional: remove the button border
        registerButton.setBorderPainted(false);
        adminButton.setBorderPainted(false);

        // Add action listeners
        loginButton.addActionListener(this::performLogin);
        registerButton.addActionListener(e -> {
            RegistrationFrame registrationFrame = new RegistrationFrame();
            registrationFrame.setVisible(true);
            LoginFrame.this.setVisible(false); // Hide the login frame
        });
        adminButton.addActionListener(e -> {
            AdminPanel adminPanel = new AdminPanel();
            adminPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set the close operation for AdminFrame
            adminPanel.setVisible(true);
        });
    }

    private void performLogin(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Attempt to connect to the server and send login credentials
        try (Socket socket = new Socket("localhost", 6868);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Command format: "login,email,password"
            out.println("login," + email + "," + password);

            // Await and process the server's response
            String response = in.readLine();
            if ("success".equals(response)) {
                JOptionPane.showMessageDialog(this, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Proceed to another frame if login is successful
                // For example, to a dashboard frame
                this.dispose(); // Close the login window
                DashboardFrame dashboardFrame = new DashboardFrame();
                dashboardFrame.setVisible(true); // Assume you have a DashboardFrame for the next window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error connecting to the server: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}