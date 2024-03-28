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
        setSize(600, 600); // Ensure the window has a fixed size
    }

    private void initializeComponents(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        emailField = new JTextField(10);
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Login");
        adminButton = new JButton("Admin Panel");
        registerButton = new JButton("Register");

        emailField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setHorizontalAlignment(JTextField.CENTER);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("ResolveIT.png"));
        // Scale it to fit a specific size
        Image scaledImage = originalIcon.getImage().getScaledInstance(375, 375, Image.SCALE_SMOOTH);
        ImageIcon logo = new ImageIcon(scaledImage);
        // Add the resized logo to the JLabel
        JLabel logoLabel = new JLabel(logo);
        gbc.anchor = GridBagConstraints.NORTH; // This will align the logo to the top center
        panel.add(logoLabel, gbc);

        panel.add(new JLabel("Username", SwingConstants.CENTER), gbc);
        panel.add(emailField, gbc);
        panel.add(new JLabel("Password", SwingConstants.CENTER), gbc);
        panel.add(passwordField, gbc);

        gbc.insets = new Insets(10, 0, 0, 0); // Top and bottom padding
        System.out.println("\n\n"); // Two new lines for spacing

        panel.add(loginButton, gbc);

        loginButton.setBackground(new Color(207, 216, 220));
        loginButton.setForeground(Color.black);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);

        adminButton.setBackground(new Color(207, 216, 220));
        adminButton.setForeground(Color.black);
        adminButton.setOpaque(true);
        adminButton.setBorderPainted(false);
        loginButton.addActionListener(this::performLogin);
        // Commented out code is retained for reference but will not be executed.
    registerButton = new JButton("Register");
        registerButton.setBackground(new Color(207, 216, 220)); // Corrected
        registerButton.setForeground(Color.BLACK);
        registerButton.setOpaque(true);
    registerButton.setBorderPainted(false);

        // Adding action listeners

    registerButton.addActionListener(e -> {
        RegistrationFrame registrationFrame = new RegistrationFrame();
        registrationFrame.setVisible(true);
        LoginFrame.this.setVisible(false);
    });
        adminButton.addActionListener(e -> {
            AdminPanel adminPanel = new AdminPanel();
            adminPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            adminPanel.setVisible(true);
        });

        // Admin button placed separately from the login button for clarity and potential different alignment.
        JPanel adminButtonPanel = new JPanel();
        adminButtonPanel.add(adminButton);
        gbc.fill = GridBagConstraints.NONE; // Reset to default if you want the admin panel button not stretched.
        panel.add(adminButtonPanel, gbc);
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