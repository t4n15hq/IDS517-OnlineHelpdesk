import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Helpdesk Dashboard");
        setSize(600, 400); // Set initial size
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use a BorderLayout as the layout for this frame
        setLayout(new BorderLayout(10, 10));

        // Welcome label at the top
        JLabel welcomeLabel = new JLabel("Welcome to the Helpdesk System!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // View Requests Button
        JButton viewRequestsButton = new JButton("View Requests");
        viewRequestsButton.addActionListener(e -> {
            ViewRequestsFrame viewRequestsFrame = new ViewRequestsFrame();
            viewRequestsFrame.setVisible(true);
        });
        buttonPanel.add(viewRequestsButton);

        // Create Request Button
        JButton createRequestButton = new JButton("Create Request");
        createRequestButton.addActionListener(e -> {
            CreateRequestFrame createRequestFrame = new CreateRequestFrame();
            createRequestFrame.setVisible(true);
        });
        buttonPanel.add(createRequestButton);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.CENTER);

        // Ensure the window is correctly displayed
        pack();
        setVisible(true);
    }
}
