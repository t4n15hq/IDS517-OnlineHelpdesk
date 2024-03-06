import javax.swing.SwingUtilities;

public class HelpdeskApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Initialize the login frame here
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}
