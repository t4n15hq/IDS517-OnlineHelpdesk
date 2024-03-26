import java.util.List;

public class ServiceRequestHandler {
    private final EmailService emailService;

    public ServiceRequestHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    public void processPendingRequests() {
        List<ServiceRequest> pendingRequests = ServiceRequest.viewRequestsByStatus("Pending");
        for (ServiceRequest request : pendingRequests) {
            sendEmailToHelpdesk(request);
        }
    }

    private void sendEmailToHelpdesk(ServiceRequest request) {
        String subject = "New Service Request Submitted: " + request.getProblem();
        String content = String.format(
                "A new service request has been submitted with the following details:\n\n" +
                        "Problem: %s\n" +
                        "Description: %s\n" +
                        "Submitted By ID: %d\n", // Adjust according to your User management system
                request.getProblem(),
                request.getDescription(),
                request.getSubmittedBy()
        );

        // Here we send the email and handle any exceptions that might occur
        try {
            emailService.sendEmail("ids517helpdesk@gmail.com", subject, content);
            ServiceRequest.updateRequestStatus(request.getRequestID(), "Notified to Helpdesk");
        } catch (Exception e) { // Changed to a generic Exception to catch any that might occur
            e.printStackTrace(); // Here we handle the exception, such as logging it
        }
    }
}
