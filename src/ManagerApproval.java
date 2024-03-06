import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ManagerApproval {
    private int approvalID;
    private int requestID;
    private int approvedBy;
    private String approvalStatus;
    private String approvalComment;

    // Constructors, getters, and setters

    // Method to approve or reject a service request
    public static boolean approveRejectRequest(int requestID, int managerID, String approvalStatus, String approvalComment) {
        // Step 1: Update the status of the service request
        String updateRequestSql = "UPDATE ServiceRequests SET Status = ? WHERE RequestID = ?";
        boolean requestUpdated = DatabaseHelper.executeUpdate(updateRequestSql, new Object[]{approvalStatus, requestID});

        // Step 2: Record the manager's decision
        String insertApprovalSql = "INSERT INTO ManagerApprovals(RequestID, ApprovedBy, ApprovalStatus, ApprovalComment) VALUES(?,?,?,?)";
        boolean approvalRecorded = DatabaseHelper.executeUpdate(insertApprovalSql, new Object[]{requestID, managerID, approvalStatus, approvalComment});

        return requestUpdated && approvalRecorded;
    }
}
