import javax.swing.*;
import java.awt.*;

public class KnowledgeBaseFrame extends JFrame {
    public KnowledgeBaseFrame() {
        setTitle("Knowledge Base // FAQ");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeComponents();
    }

    private void initializeComponents() {
        // Use JEditorPane for HTML content
        JEditorPane knowledgeBasePane = new JEditorPane();
        knowledgeBasePane.setContentType("text/html"); // Set content type to HTML
        knowledgeBasePane.setEditable(false); // Make it non-editable
        knowledgeBasePane.setBackground(new Color(255, 255, 255)); // White background

        // Setting HTML content
        knowledgeBasePane.setText(getHTMLKnowledgeBaseContent());
        knowledgeBasePane.setCaretPosition(0); // Scroll back to the top

        // Wrap JEditorPane in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(knowledgeBasePane);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Set a light green background for the frame
        getContentPane().setBackground(new Color(204, 255, 204)); // Light green
    }

    private String getHTMLKnowledgeBaseContent() {

        String styles = "<style>" +
                "body { background-color: #e8f5e9; }" + // Light green background
                "h2 { color: #2e7d32; }" + // Dark green color for headings
                "p { background-color: #ffffff; padding: 10px; margin: 8px 0; border-left: 5px solid #4caf50; }" + // White background for paragraphs with green border on the left
                "</style>";
        // HTML formatted knowledge base content
        return "<html><body style='font-family: Arial, sans-serif;'>" +
                "<h2>Knowledge Base</h2>" +

                "<p><b>Network Connectivity Issue</b><br>" +
                "<b>Q:</b> What should I do if my computer cannot connect to the office network?<br>" +
                "<b>A:</b> Ensure your device's Wi-Fi is on and try reconnecting to the office network. If you're using a wired connection, check if the Ethernet cable is plugged in properly. Restart your device as a first step of troubleshooting.</p>" +

                "<p><b>Software Installation Problem</b><br>" +
                "<b>Q:</b> I need specific software for my work. How can I get it installed?<br>" +
                "<b>A:</b> Submit a ticket to the IT helpdesk with details of the required software. Please note that software installation might need approval from your manager.</p>" +

                "<p><b>Hardware Malfunction</b><br>" +
                "<b>Q:</b> My office laptop is not working properly. Who should I contact?<br>" +
                "<b>A:</b> Report the issue to the IT helpdesk with details of the malfunction. Do not attempt to fix hardware issues yourself to avoid voiding any warranties or causing further damage.</p>" +

                "<p><b>Password Reset Request</b><br>" +
                "<b>Q:</b> I forgot my login password. How can I reset it?<br>" +
                "<b>A:</b> Use the \"Forgot Password\" link on the login screen, if available, or contact the IT helpdesk for a reset. You may need to verify your identity.</p>" +

                "<p><b>Printer Not Working</b><br>" +
                "<b>Q:</b> The office printer is not printing. What should I check?<br>" +
                "<b>A:</b> Ensure the printer is turned on and connected to the network. Check for paper jams or low toner. If the problem persists, report it to the IT helpdesk.</p>" +

                "<p><b>Email Configuration Issue</b><br>" +
                "<b>Q:</b> How do I set up my office email on my personal device?<br>" +
                "<b>A:</b> Refer to the email setup guide provided by the IT department, which includes step-by-step instructions for different devices. Contact the IT helpdesk if you encounter issues.</p>" +

                "<p><b>File Access Permission Problem</b><br>" +
                "<b>Q:</b> I can't access certain files on the network drive. What should I do?<br>" +
                "<b>A:</b> If you believe you should have access, contact the file owner or submit a request to the IT helpdesk to adjust your permissions.</p>" +

                "<p><b>Slow System Performance</b><br>" +
                "<b>Q:</b> My computer is running slowly. How can I improve its performance?<br>" +
                "<b>A:</b> Close unused applications to free up system resources. Consider restarting your computer. If issues persist, contact the IT helpdesk for further assistance.</p>" +

                "<p><b>Website Access Problem</b><br>" +
                "<b>Q:</b> I'm unable to access a website I need for work. What should I do?<br>" +
                "<b>A:</b> Verify your internet connection and try accessing the website from a different browser. If the website is still not accessible, contact the IT helpdesk as it might be blocked by the company's firewall.</p>" +

                "<p><b>Application Crashing</b><br>" +
                "<b>Q:</b> An application I use frequently keeps crashing. What should I do?<br>" +
                "<b>A:</b> Make sure the application is updated to the latest version. If the problem continues, report the issue to the IT helpdesk with specific error messages or behaviors.</p>" +

                "<p><b>Data Backup Request</b><br>" +
                "<b>Q:</b> How can I ensure my work data is backed up?<br>" +
                "<b>A:</b> Follow the company's data backup procedures, which may include saving files to a designated network drive or using approved cloud storage services. For specific backup solutions, contact the IT helpdesk.</p>" +

                "<p><b>System Update Request</b><br>" +
                "<b>Q:</b> Should I update my operating system when prompted?<br>" +
                "<b>A:</b> System updates are important for security and performance. However, coordinate with the IT helpdesk as updates may be managed centrally, especially to ensure compatibility with company software.</p>" +

                "<p><b>Account Lockout Issue</b><br>" +
                "<b>Q:</b> My account has been locked after several login attempts. What's the next step?<br>" +
                "<b>A:</b> Wait a few minutes to see if the account unlocks automatically. If not, or if you need immediate access, contact the IT helpdesk to unlock your account.</p>" +

                "</body></html>";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KnowledgeBaseFrame frame = new KnowledgeBaseFrame();
            frame.setVisible(true);
        });
    }
}