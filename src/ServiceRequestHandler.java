import java.util.List;

public class ServiceRequestHandler {
    private EmailService emailService;

    public ServiceRequestHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    // This method is called to process and send emails for all pending requests.
    public void processPendingRequests() {
        // Fetch pending service requests from the database.
        List<ServiceRequest> pendingRequests = ServiceRequest.viewRequestsByStatus("Pending");

        // Iterate through each request and send an email notification.
        for (ServiceRequest request : pendingRequests) {
            sendEmailToHelpdesk(request);
        }
    }

    // Sends an email for a specific service request.
    private void sendEmailToHelpdesk(ServiceRequest request) {
        // Email subject line
        String subject = "New Service Request Submitted: " + request.getProblem();

        // Email content
        String content = String.format(
                "A new service request has been submitted with the following details:\n\n" +
                        "Problem: %s\n" +
                        "Description: %s\n" +
                        "Submitted By ID: %d\n" + // Adjust according to your User management system
                        // You might need to fetch the user's name or other details if required
                        "Status: Pending\n", // Adding status for clarity
                request.getProblem(),
                request.getDescription(),
                request.getSubmittedBy()
        );

        // Send email notification
        emailService.sendEmail("ids517helpdesk@gmail.com", subject, content); // Use your helpdesk email address here
    }
}
