import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class DashboardFrame extends JFrame {
    private JButton backButton;
    public DashboardFrame() {
        setTitle("ResolveIT Dashboard");
        setSize(400, 400); // Set initial size
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 128, 0)); // Set a green background

        // Use a BorderLayout as the layout for this frame
        setLayout(new BorderLayout(10, 10));

        // Welcome label at the top
        JLabel welcomeLabel = new JLabel("Welcome to ResolveIT!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.BLACK); // Set text color to black for contrast
        add(welcomeLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false); // Make the panel transparent to show the frame's background

        // View Requests Button
        JButton viewRequestsButton = new JButton("View Requests");
        styleButton(viewRequestsButton);
        viewRequestsButton.addActionListener(e -> {
            ViewRequestsFrame viewRequestsFrame = new ViewRequestsFrame();
            viewRequestsFrame.setVisible(true);
        });
        buttonPanel.add(viewRequestsButton);

        // Create Request Button
        JButton createRequestButton = new JButton("Create Request");
        styleButton(createRequestButton);
        createRequestButton.addActionListener(e -> {
            CreateRequestFrame createRequestFrame = new CreateRequestFrame();
            createRequestFrame.setVisible(true);
        });
        buttonPanel.add(createRequestButton);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.CENTER);

        // Back Button
        backButton = new JButton("Log Out");
        styleButton(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle back button action here
                // For example, navigate back to the login frame
                dispose(); // Close the dashboard frame
                new LoginFrame().setVisible(true); // Show the login frame
            }
        });

        // Add the back button to the bottom of the frame
        add(backButton, BorderLayout.SOUTH);

        // Ensure the window is correctly displayed
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.WHITE); // Set background color to white
        button.setForeground(Color.BLACK); // Set text color to black
        button.setFocusPainted(false); // Remove focus border
        button.setFont(new Font("Dialog", Font.BOLD, 16)); // Font styling
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}