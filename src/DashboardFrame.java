import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("ResolveIT Dashboard");
        setSize(400, 300); // Set initial size
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 128, 0)); // Set a green background

        // Use a BorderLayout as the layout for this frame
        setLayout(new BorderLayout(10, 10));

        // Welcome label at the top
        JLabel welcomeLabel = new JLabel("Welcome to the ResolveIT!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
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

        // Ensure the window is correctly displayed
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.WHITE); // Set background color to white
        button.setForeground(Color.BLACK); // Set text color to black
        button.setFocusPainted(false); // Remove focus border
        button.setFont(new Font("Serif", Font.BOLD, 16)); // Font styling
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
